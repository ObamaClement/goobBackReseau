package com.example.service.impl;

import com.example.entity.User;
import com.example.integration.yowyob.YowyobOrganizationClient;
import com.example.integration.yowyob.dto.CreateBusinessActorRequest;
import com.example.integration.yowyob.dto.CreateOrganizationRequest;
import com.example.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

/**
 * Implémentation du service gérant les profils professionnels.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final YowyobOrganizationClient organizationClient;

    // L'ID de votre organisation "parente" chez YOWYOB.
    // Pour l'instant, on le met en dur mais on pourrait le mettre en properties.
    // **CECI EST UN UUID FACTICE À REMPLACER SI LE DÉVELOPPEUR VOUS EN DONNE UN**
    @Value("${transaffil.parent-organization-id:a1b2c3d4-e5f6-7890-1234-567890abcdef}")
    private UUID parentOrganizationId;


    @Override
    public void createEditorProfileOnExternalApi(User newUser, String yowyobUserToken) {
        log.info("Préparation de la création du profil professionnel pour l'éditeur : {}", newUser.getUsername());

        // 1. Construire la requête pour le YowyobOrganizationClient
        CreateOrganizationRequest request = CreateOrganizationRequest.builder()
                .name(newUser.getBusinessName()) // Utilise le nom d'entreprise fourni à l'inscription
                .longName(newUser.getBusinessName())
                .description(newUser.getBio())
                // Ajoutez d'autres champs si nécessaire
                .build();
        
        try {
            // 2. Appeler le client pour exécuter la requête
            organizationClient.createEditorProfile(request, yowyobUserToken, parentOrganizationId);
            log.info("Le profil professionnel pour {} a été créé avec succès sur l'API d'organisation.", newUser.getUsername());
        } catch (RestClientException e) {
            // Dans un vrai projet, il faudrait gérer cette erreur de manière plus robuste.
            // Par exemple, en annulant l'inscription de l'utilisateur de base si cette étape échoue.
            // Pour l'instant, on se contente de logger l'erreur.
            log.error("Échec de la création du profil professionnel pour l'éditeur {} : {}",
                    newUser.getUsername(), e.getMessage());

            // On propage l'exception pour que la transaction globale de l'inscription soit annulée.
            throw new IllegalStateException("Impossible de finaliser le profil de l'éditeur sur le service externe.", e);
        }
    }


    @Override
    public void createAffiliateProfileOnExternalApi(User newUser, String yowyobUserToken) {
        // LOG D'INTENTION
        log.info("Préparation de la création du profil Business Actor pour l'affilié : {}", newUser.getUsername());

        // 1. Construire la requête pour le YowyobOrganizationClient
        CreateBusinessActorRequest request = CreateBusinessActorRequest.builder()
                .email(newUser.getEmail())
                .phoneNumber(null) // Le SignUpRequestDto n'a pas ce champ, à ajouter si nécessaire
                .firstName(null)   // Le SignUpRequestDto n'a pas ces champs non plus
                .lastName(null)
                .biography("Affiliate on TransAffil platform")
                .build();
        
        try {
            // 2. Appeler le client pour exécuter la requête
            organizationClient.createBusinessActor(request, yowyobUserToken);
            // LOG DE SUCCÈS
            log.info("Profil Business Actor pour {} créé avec succès sur YOWYOB.", newUser.getUsername());
        } catch (RestClientException e) {
            // LOG D'ERREUR
            log.error("Échec de la création du profil Business Actor pour l'affilié {} : {}",
                    newUser.getUsername(), e.getMessage());
            throw new IllegalStateException("Impossible de finaliser le profil de l'affilié sur le service externe.", e);
        }
    }

    
}