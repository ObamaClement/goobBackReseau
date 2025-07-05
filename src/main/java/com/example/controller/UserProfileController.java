package com.example.controller;

import com.example.dto.response.CollaborationResponseDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.CollaborationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Controller pour les endpoints liés à l'utilisateur authentifié ("moi").
 */
@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final CollaborationService collaborationService;
    private final UserRepository userRepository; // On a besoin de ce repo pour trouver l'ID de l'utilisateur

    /**
     * Endpoint pour que l'utilisateur connecté récupère la liste de ses collaborations.
     * La logique s'adapte en fonction de son rôle.
     * Accessible aux éditeurs ET aux affiliés.
     *
     * @param principal L'objet représentant l'utilisateur authentifié, injecté par Spring Security.
     * @return Une liste de ses collaborations.
     */
    @GetMapping("/collaborations")
    @PreAuthorize("hasAuthority('ROLE_EDITOR') or hasAuthority('ROLE_AFFILIATE')")
    public ResponseEntity<List<CollaborationResponseDto>> getMyCollaborations(Principal principal) {
        
        // 1. Récupérer l'utilisateur complet depuis la base de données
        String username = principal.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        
        UUID userId = currentUser.getId();

        // 2. Adapter la logique en fonction du rôle de l'utilisateur
        List<CollaborationResponseDto> collaborations;
        
        // L'objet Authentication contient plus d'infos, y compris les rôles.
        Authentication authentication = (Authentication) principal;
        
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EDITOR"))) {
            // Si c'est un éditeur, on cherche ses collaborations en tant qu'éditeur
            collaborations = collaborationService.getCollaborationsForEditor(userId);
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AFFILIATE"))) {
            // Si c'est un affilié, on cherche ses collaborations en tant qu'affilié
            collaborations = collaborationService.getCollaborationsForAffiliate(userId);
        } else {
            // Cas improbable (l'utilisateur a passé @PreAuthorize mais n'a aucun des deux rôles)
            collaborations = Collections.emptyList();
        }

        return ResponseEntity.ok(collaborations);
    }
}