package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente une vente effectuée sur la plateforme.
 * C'est notre "livre de comptes" interne.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Le produit qui a été vendu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // L'utilisateur qui a acheté le produit
    // NOTE : Peut être null si on autorise les achats anonymes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    // L'affilié qui a apporté la vente (grâce à son code promo)
    // Ce champ est optionnel.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id")
    private User affiliate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid; // Le montant final payé par le client

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal editorEarnings; // Montant qui revient à l'éditeur

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal affiliateCommission; // Montant qui revient à l'affilié

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal platformFee; // Montant qui revient à la plateforme

    @Column(nullable = false)
    private String paymentGatewayTransactionId; // L'ID de transaction de YOWYOB, pour la réconciliation

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}