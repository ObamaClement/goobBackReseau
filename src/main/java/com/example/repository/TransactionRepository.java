package com.example.repository;

import com.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'accès aux données de l'entité Transaction.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // --- Méthodes pour les dashboards (à utiliser plus tard) ---

    /**
     * Trouve toutes les transactions apportées par un affilié spécifique.
     * Utile pour le dashboard de l'affilié.
     * @param affiliateId L'ID de l'affilié.
     * @return Une liste de ses transactions.
     */
    List<Transaction> findByAffiliateId(UUID affiliateId);

    /**
     * Trouve toutes les transactions liées aux produits d'un éditeur spécifique.
     * Utile pour le dashboard de l'éditeur.
     * @param editorId L'ID de l'éditeur (via product.editor.id).
     * @return Une liste des transactions pour les produits de cet éditeur.
     */
    List<Transaction> findByProductEditorId(UUID editorId);

}