package com.example.controller;

import com.example.dto.request.CreateProductRequestDto;
import com.example.dto.response.ProductResponseDto;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1") // Changement de la route de base pour plus de flexibilité
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Endpoint pour créer un nouveau produit.
     * Accessible uniquement par les utilisateurs ayant le rôle 'ROLE_EDITOR'.
     *
     * @param requestDto Les données du produit à créer.
     * @return Le produit créé avec un statut 201.
     */
    @PostMapping("/products")
    @PreAuthorize("hasAuthority('ROLE_EDITOR')") // Sécurisation de l'endpoint
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductRequestDto requestDto) {
        // Récupère le nom d'utilisateur de l'utilisateur authentifié depuis le contexte de sécurité.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        ProductResponseDto createdProduct = productService.createProduct(requestDto, username);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Endpoint PUBLIC pour récupérer tous les produits de la marketplace.
     * @return La liste de tous les produits.
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint PUBLIC pour récupérer un produit spécifique par son ID.
     * @param id L'ID du produit.
     * @return Le produit trouvé.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        System.out.println("CONTROLLER: Requête reçue pour les avec l'ID: {}"+ id);
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    /**
     * Endpoint PUBLIC pour lister tous les produits d'un éditeur spécifique.
     * @param editorId L'ID de l'éditeur.
     * @return La liste de ses produits.
     */
    @GetMapping("/editors/{editorId}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByEditor(@PathVariable UUID editorId) {
        System.out.println("CONTROLLER: Requête reçue pour les produits de l'éditeur avec l'ID: {}"+ editorId);
        List<ProductResponseDto> products = productService.getProductsByEditor(editorId);
        return ResponseEntity.ok(products);
    }
}