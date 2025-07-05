package com.example.controller;

import com.example.dto.request.CollaborationRequestDto;
import com.example.dto.response.CollaborationResponseDto;
import com.example.service.CollaborationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller pour gérer les collaborations entre Éditeurs et Annonceurs.
 */
@RestController
@RequestMapping("/api/v1/collaborations")
@RequiredArgsConstructor
@Slf4j
public class CollaborationController {

    private final CollaborationService collaborationService;

    /**
     * Endpoint pour qu'un Annonceur (affilié) demande une collaboration avec un Éditeur.
     * Accessible uniquement aux utilisateurs avec le rôle ROLE_AFFILIATE.
     *
     * @param requestDto Le corps de la requête contenant l'ID de l'éditeur ciblé.
     * @return Les détails de la collaboration créée en attente.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_AFFILIATE')")
    public ResponseEntity<CollaborationResponseDto> requestCollaboration(@Valid @RequestBody CollaborationRequestDto requestDto) {
        String affiliateUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Demande de collaboration de l'affilié {} pour l'éditeur {}", affiliateUsername, requestDto.getEditorId());
        
        CollaborationResponseDto response = collaborationService.requestCollaboration(requestDto, affiliateUsername);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint pour qu'un Éditeur approuve une demande de collaboration.
     * Accessible uniquement aux utilisateurs avec le rôle ROLE_EDITOR.
     *
     * @param collaborationId L'ID de la collaboration à approuver.
     * @return Les détails de la collaboration mise à jour.
     */
    @PutMapping("/{collaborationId}/approve")
    @PreAuthorize("hasAuthority('ROLE_EDITOR')")
    public ResponseEntity<CollaborationResponseDto> approveCollaboration(@PathVariable UUID collaborationId) {
        String editorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Approbation de la collaboration {} par l'éditeur {}", collaborationId, editorUsername);
        
        CollaborationResponseDto response = collaborationService.approveCollaboration(collaborationId, editorUsername);

        return ResponseEntity.ok(response);
    }
}