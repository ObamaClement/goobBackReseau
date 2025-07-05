package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente un produit. Chaque produit est possédé par un et un seul utilisateur (Éditeur).
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private boolean inStock;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // --- RELATION CLÉ ---
    // Cette annotation crée la clé étrangère dans la base de données.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: on ne charge l'éditeur que si on en a besoin.
    @JoinColumn(name = "editor_id", nullable = false) // Nom de la colonne FK. `nullable = false` garantit qu'un produit a toujours un éditeur.
    private User editor;

    // --- CALLBACKS DE CYCLE DE VIE JPA ---
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}