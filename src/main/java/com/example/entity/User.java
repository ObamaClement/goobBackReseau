package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // -- Champs spécifiques à l'Éditeur (Entreprise) --
    private String businessName;
    @Column(columnDefinition = "TEXT")
    private String bio;

    // -- Champs spécifiques à l'Annonceur (Influenceur) --
    private String profileImage;
    private Long followers;
    
    // --- RELATIONS ---
    
    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // CORRECTION
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // CORRECTION
    private Set<Collaboration> collaborationsAsEditor = new HashSet<>();

    @OneToMany(mappedBy = "affiliate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // CORRECTION
    private Set<Collaboration> collaborationsAsAffiliate = new HashSet<>();
}