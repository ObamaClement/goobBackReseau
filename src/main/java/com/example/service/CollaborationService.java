package com.example.service;

import com.example.dto.request.CollaborationRequestDto;
import com.example.dto.response.CollaborationResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interface pour le service gérant la logique métier des collaborations.
 */
@Service
public interface CollaborationService {

    /**
     * Permet à un affilié authentifié de faire une demande de collaboration à un éditeur.
     *
     * @param requestDto      Le DTO contenant l'ID de l'éditeur ciblé.
     * @param affiliateUsername Le nom d'utilisateur de l'affilié qui fait la demande (récupéré du token).
     * @return Les détails de la collaboration créée avec le statut PENDING.
     */
    CollaborationResponseDto requestCollaboration(CollaborationRequestDto requestDto, String affiliateUsername);

    /**
     * Permet à un éditeur authentifié d'approuver une demande de collaboration.
     *
     * @param collaborationId L'ID de la collaboration à approuver.
     * @param editorUsername  Le nom d'utilisateur de l'éditeur qui approuve (pour vérification de droit).
     * @return Les détails de la collaboration mise à jour avec le statut ACTIVE.
     */
    CollaborationResponseDto approveCollaboration(UUID collaborationId, String editorUsername);
    
    /**
     * Récupère toutes les collaborations pour un éditeur donné.
     * Utile pour le dashboard de l'éditeur.
     *
     * @param editorId L'ID de l'éditeur.
     * @return Une liste de ses collaborations.
     */
    List<CollaborationResponseDto> getCollaborationsForEditor(UUID editorId);
    
    /**
     * Récupère toutes les collaborations pour un affilié donné.
     * Utile pour le dashboard de l'affilié.
     *
     * @param affiliateId L'ID de l'affilié.
     * @return Une liste de ses collaborations.
     */
    List<CollaborationResponseDto> getCollaborationsForAffiliate(UUID affiliateId);

    // D'autres méthodes pourraient être ajoutées plus tard, comme :
    // rejectCollaboration(UUID collaborationId, String editorUsername);
    // terminateCollaboration(UUID collaborationId, String username);
}