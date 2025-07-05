package com.example.service.impl;

import com.example.dto.request.CreateProductRequestDto;
import com.example.dto.response.ProductResponseDto;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.exception.ResourceNotFoundException; // <-- NOUVEL IMPORT
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository; // On a besoin de ce repository

    @Override
    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto, String username) {
        // --- MODIFICATION : Récupérer l'éditeur ---
        User editor = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur (éditeur) non trouvé avec le nom : " + username));
        
        // On pourrait ajouter une vérification pour s'assurer qu'il a le bon rôle,
        // mais @PreAuthorize dans le controller est plus efficace pour ça.

        Product product = mapToEntity(requestDto);
        product.setEditor(editor); // On lie le produit à l'éditeur authentifié

        Product savedProduct = productRepository.save(product);

        return mapToResponseDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                // --- MODIFICATION : Utiliser notre exception personnalisée ---
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + id));
        
        return mapToResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    // --- Méthode de l'interface non encore implémentée
    // TODO: Implémenter la logique pour cette méthode
    @Override
    public List<ProductResponseDto> getProductsByEditor(UUID editorId) {
        if (!userRepository.existsById(editorId)) {
            throw new ResourceNotFoundException("Éditeur non trouvé avec l'ID : " + editorId);
        }
        return productRepository.findByEditorId(editorId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // --- Méthodes de mapping privées (Logique de conversion) ---

    private Product mapToEntity(CreateProductRequestDto requestDto) {
        return Product.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .imageUrl(requestDto.getImageUrl())
                .category(requestDto.getCategory())
                .inStock(requestDto.getInStock())
                .commissionRate(requestDto.getCommissionRate())
                .build();
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(product.getId());
        responseDto.setTitle(product.getTitle());
        responseDto.setDescription(product.getDescription());
        responseDto.setPrice(product.getPrice());
        responseDto.setImageUrl(product.getImageUrl());
        responseDto.setCategory(product.getCategory());
        responseDto.setInStock(product.isInStock());
        responseDto.setCommissionRate(product.getCommissionRate());
        responseDto.setCreatedAt(product.getCreatedAt());
        return responseDto;
    }
}