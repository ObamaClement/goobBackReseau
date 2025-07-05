package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * DTO représentant la réponse de notre API après une tentative d'achat.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // N'inclut pas les champs null dans le JSON
public class PurchaseResponseDto {

    /**
     * L'ID de notre transaction locale, créée dans notre base de données.
     * Utile pour des requêtes de suivi ultérieures.
     */
    private UUID localTransactionId;

    /**
     * L'ID de transaction renvoyé par la passerelle de paiement YOWYOB.
     * Essentiel pour la réconciliation et la vérification du statut.
     */
    private String paymentGatewayTransactionId;

    /**
     * Le statut initial de la transaction renvoyé par YOWYOB (ex: "PENDING", "SUCCESS").
     */
    private String transactionStatus;

    /**
     * Un message lisible pour le frontend (ex: "Paiement initié. Veuillez confirmer sur votre mobile.").
     */
    private String message;

    /**
     * URL de redirection potentielle si la passerelle de paiement en fournit une.
     * (Optionnel, dépend de l'API de paiement).
     */
    private String redirectUrl;
}