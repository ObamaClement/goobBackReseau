package com.example.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse API lors de la récupération d'un produit.
 * Représente la structure de données "propre" et "sûre" envoyée au frontend.
 */
@Data
public class ProductResponseDto {

    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private boolean inStock;
    private BigDecimal commissionRate;
    private LocalDateTime createdAt;
    
    // On pourrait choisir de ne pas exposer updatedAt si ce n'est pas utile au frontend.
    // private LocalDateTime updatedAt; 
}