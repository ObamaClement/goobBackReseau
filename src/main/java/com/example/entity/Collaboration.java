package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité de jointure représentant la collaboration entre un Éditeur et un Annonceur (Affilié).
 * Modélise une relation Many-to-Many entre les utilisateurs.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "collaborations")
public class Collaboration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // --- RELATION VERS L'ÉDITEUR ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id", nullable = false)
    private User editor;

    // --- RELATION VERS L'ANNONCEUR (AFFILIÉ) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false)
    private User affiliate;

    // --- INFORMATIONS SUR LA COLLABORATION ---

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaborationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime approvedAt; // La date à laquelle la collaboration est devenue active
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (this.status == null) {
            // Un statut par défaut si aucun n'est fourni
            this.status = CollaborationStatus.PENDING; 
        }
    }
}