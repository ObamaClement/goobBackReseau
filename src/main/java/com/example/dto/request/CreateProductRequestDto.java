package com.example.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) pour la requête de création d'un nouveau produit.
 * Contient les validations pour les données entrantes.
 */
@Data // Annotation Lombok pour générer getters, setters, etc.
public class CreateProductRequestDto {

    @NotBlank(message = "Le titre ne peut pas être vide.")
    @Size(max = 150, message = "Le titre ne doit pas dépasser 150 caractères.")
    private String title;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères.")
    private String description;

    @NotNull(message = "Le prix est obligatoire.")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0.")
    private BigDecimal price;

    @Size(max = 255, message = "L'URL de l'image est trop longue.")
    private String imageUrl;

    @NotBlank(message = "La catégorie est obligatoire.")
    private String category;

    @NotNull(message = "Le statut 'en stock' est obligatoire.")
    private Boolean inStock;

    @NotNull(message = "Le taux de commission est obligatoire.")
    @DecimalMin(value = "0.1", message = "Le taux de commission doit être d'au moins 0.1%.")
    private BigDecimal commissionRate;
}