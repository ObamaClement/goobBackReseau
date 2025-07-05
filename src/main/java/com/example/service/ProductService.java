package com.example.service;

import com.example.dto.request.CreateProductRequestDto;
import com.example.dto.response.ProductResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Interface pour le service de gestion des produits.
 * Définit le contrat pour toutes les opérations métier liées aux produits.
 */
@Service
public interface ProductService {

    /**
     * Crée un nouveau produit pour l'utilisateur authentifié.
     *
     * @param requestDto DTO contenant les informations du produit à créer.
     * @param username Le nom d'utilisateur de l'éditeur qui crée le produit.
     * @return Le DTO du produit qui a été créé.
     */
    ProductResponseDto createProduct(CreateProductRequestDto requestDto, String username);

    /**
     * Récupère un produit par son identifiant unique.
     *
     * @param id L'UUID du produit à récupérer.
     * @return Le DTO du produit trouvé.
     * @throws com.example.exception.ResourceNotFoundException si aucun produit n'est trouvé.
     */
    ProductResponseDto getProductById(UUID id);

    /**
     * Récupère la liste de tous les produits.
     *
     * @return Une liste de DTOs de tous les produits.
     */
    List<ProductResponseDto> getAllProducts();

    /**
     * Récupère la liste de tous les produits pour un éditeur spécifique.
     *
     * @param editorId L'ID de l'éditeur.
     * @return Une liste de DTOs des produits de cet éditeur.
     */
    List<ProductResponseDto> getProductsByEditor(UUID editorId);
}