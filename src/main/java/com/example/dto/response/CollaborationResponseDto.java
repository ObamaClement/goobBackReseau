package com.example.dto.response;

import com.example.entity.CollaborationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO représentant les informations d'une collaboration à renvoyer au client.
 */
@Data
@Builder
public class CollaborationResponseDto {

    private UUID id;
    private CollaborationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    // Informations sur l'Éditeur
    private UserInfo editor;

    // Informations sur l'Annonceur (Affilié)
    private UserInfo affiliate;

    /**
     * Sous-DTO pour ne renvoyer que les informations publiques de base
     * d'un utilisateur, évitant ainsi de fuiter des données sensibles.
     */
    @Data
    @Builder
    public static class UserInfo {
        private UUID userId;
        private String username;
        // On pourrait ajouter d'autres champs publics comme businessName ou profileImage ici
    }
}