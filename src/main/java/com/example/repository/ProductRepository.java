package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Recherche des produits par catégorie.
     */
    List<Product> findByCategory(String category);
    
    /**
     * Recherche tous les produits appartenant à un éditeur spécifique.
     * Le nom 'findByEditorId' est interprété par Spring pour créer la bonne requête.
     *
     * @param editorId L'ID de l'utilisateur éditeur.
     * @return Une liste de produits pour cet éditeur.
     */
    List<Product> findByEditorId(UUID editorId);
}