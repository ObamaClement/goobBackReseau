package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO pour une requête de demande de collaboration de la part d'un Annonceur (Affilié).
 */
@Data
public class CollaborationRequestDto {

    /**
     * L'ID de l'utilisateur Éditeur avec lequel l'affilié souhaite collaborer.
     * Ce champ est obligatoire.
     */
    @NotNull(message = "L'ID de l'éditeur est obligatoire.")
    private UUID editorId;

}