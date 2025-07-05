package com.example.service.impl;

import com.example.dto.request.CollaborationRequestDto;
import com.example.dto.response.CollaborationResponseDto;
import com.example.entity.Collaboration;
import com.example.entity.CollaborationStatus;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.CollaborationRepository;
import com.example.repository.UserRepository;
import com.example.service.CollaborationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollaborationServiceImpl implements CollaborationService {

    private final CollaborationRepository collaborationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CollaborationResponseDto requestCollaboration(CollaborationRequestDto requestDto, String affiliateUsername) {
        User affiliate = userRepository.findByUsername(affiliateUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Affilié non trouvé: " + affiliateUsername));
        
        User editor = userRepository.findById(requestDto.getEditorId())
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur non trouvé avec l'ID: " + requestDto.getEditorId()));

        if (editor.getRole() != UserRole.ROLE_EDITOR) {
            throw new IllegalArgumentException("Vous ne pouvez demander une collaboration qu'avec un éditeur.");
        }

        // TODO: Ajouter une vérification pour s'assurer qu'une demande n'existe pas déjà.

        Collaboration newCollaboration = Collaboration.builder()
                .affiliate(affiliate)
                .editor(editor)
                .status(CollaborationStatus.PENDING)
                .build();
        
        Collaboration savedCollaboration = collaborationRepository.save(newCollaboration);
        log.info("Nouvelle demande de collaboration créée par {} pour {}", affiliate.getUsername(), editor.getUsername());

        return mapToDto(savedCollaboration);
    }

    @Override
    @Transactional
    public CollaborationResponseDto approveCollaboration(UUID collaborationId, String editorUsername) {
        Collaboration collaboration = collaborationRepository.findById(collaborationId)
                .orElseThrow(() -> new ResourceNotFoundException("Collaboration non trouvée avec l'ID: " + collaborationId));
        
        // Vérification de sécurité cruciale : l'utilisateur qui approuve est-il bien l'éditeur de cette collaboration ?
        if (!collaboration.getEditor().getUsername().equals(editorUsername)) {
            throw new AccessDeniedException("Vous n'avez pas la permission d'approuver cette collaboration.");
        }
        
        if (collaboration.getStatus() != CollaborationStatus.PENDING) {
            throw new IllegalStateException("Seule une collaboration en attente (PENDING) peut être approuvée.");
        }

        collaboration.setStatus(CollaborationStatus.ACTIVE);
        collaboration.setApprovedAt(LocalDateTime.now());
        
        Collaboration updatedCollaboration = collaborationRepository.save(collaboration);
        log.info("Collaboration ID {} approuvée par l'éditeur {}", collaborationId, editorUsername);

        return mapToDto(updatedCollaboration);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollaborationResponseDto> getCollaborationsForEditor(UUID editorId) {
        return collaborationRepository.findByEditorId(editorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollaborationResponseDto> getCollaborationsForAffiliate(UUID affiliateId) {
        return collaborationRepository.findByAffiliateId(affiliateId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- Mapper Utilitaire ---

    private CollaborationResponseDto mapToDto(Collaboration collaboration) {
        CollaborationResponseDto.UserInfo editorInfo = CollaborationResponseDto.UserInfo.builder()
                .userId(collaboration.getEditor().getId())
                .username(collaboration.getEditor().getUsername())
                .build();
        
        CollaborationResponseDto.UserInfo affiliateInfo = CollaborationResponseDto.UserInfo.builder()
                .userId(collaboration.getAffiliate().getId())
                .username(collaboration.getAffiliate().getUsername())
                .build();
        
        return CollaborationResponseDto.builder()
                .id(collaboration.getId())
                .status(collaboration.getStatus())
                .createdAt(collaboration.getCreatedAt())
                .approvedAt(collaboration.getApprovedAt())
                .editor(editorInfo)
                .affiliate(affiliateInfo)
                .build();
    }
}