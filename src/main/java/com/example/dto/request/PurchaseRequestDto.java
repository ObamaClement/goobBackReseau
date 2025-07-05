package com.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO représentant la requête d'un client pour acheter un produit.
 */
@Data
public class PurchaseRequestDto {

    // --- Détails du Produit ---
    @NotNull(message = "L'ID du produit est obligatoire.")
    private UUID productId;

    // --- Code Promo (Optionnel) ---
    private String promoCode; 

    // --- Informations de l'Acheteur pour le Paiement ---
    // Ces informations sont requises par l'API de paiement YOWYOB
    @NotBlank(message = "Le nom du payeur est obligatoire.")
    private String payerName;

    @NotBlank(message = "Le numéro de téléphone du payeur est obligatoire.")
    private String payerPhoneNumber;

    @NotBlank(message = "L'email du payeur est obligatoire.")
    @Email(message = "Le format de l'email est invalide.")
    private String payerEmail;

    // L'ID de l'utilisateur acheteur s'il est connecté (optionnel)
    // Utile pour lier la transaction à un compte client.
    private UUID buyerId;
}