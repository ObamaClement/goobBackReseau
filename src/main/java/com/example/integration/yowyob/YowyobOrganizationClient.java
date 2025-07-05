package com.example.integration.yowyob;

import com.example.integration.yowyob.dto.CreateBusinessActorRequest;
import com.example.integration.yowyob.dto.CreateOrganizationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
//import com.example.integration.yowyob.dto.CreateBusinessActorRequest; // Nouvel import


import java.util.UUID;

/**
 * Client HTTP pour communiquer avec le microservice d'organisation de YOWYOB.
 */
@Component
@Slf4j
public class YowyobOrganizationClient {

    private final RestTemplate restTemplate;
    private final String organizationServiceBaseUrl;

    // --- Injection des clés d'application ---
    @Value("${yowyob.api.app-id}")
    private String appId;
    @Value("${yowyob.api.public-key}")
    private String publicKey;
    // La clé secrète n'est pas utilisée pour l'instant car nous ne signons pas les requêtes.
    // @Value("${yowyob.api.secret-key}")
    // private String secretKey;


    public YowyobOrganizationClient(RestTemplateBuilder builder,
                                    @Value("${yowyob.api.organization.base-url}") String orgUrl) {
        this.restTemplate = builder.build();
        this.organizationServiceBaseUrl = orgUrl;
    }
    
    /**
     * Crée une organisation (ou un 'ThirdParty' de type 'Provider') pour un utilisateur éditeur.
     *
     * @param request Le DTO contenant les informations de l'organisation à créer.
     * @param userYowyobToken Le token JWT de l'utilisateur YOWYOB, prouvant son autorisation.
     * @param organizationId L'ID de l'organisation "parente", probablement l'ID de votre propre organisation TransAffil chez YOWYOB.
     */
    public void createEditorProfile(CreateOrganizationRequest request, String userYowyobToken, UUID organizationId) throws RestClientException {
        // L'endpoint exact pourrait être /organizations/{id}/third-parties/{type}
        // Il faut clarifier ce que représente {organization_id} dans ce contexte.
        // Hypothèse: c'est un ID global pour votre plateforme TransAffil.
        String url = String.format("%s/organizations/%s/third-parties/%s",
                organizationServiceBaseUrl, organizationId, request.getType());

        // 1. Créer les en-têtes avec TOUTES les informations nécessaires
        HttpHeaders headers = createAuthenticatedHeaders(userYowyobToken);
        
        HttpEntity<CreateOrganizationRequest> entity = new HttpEntity<>(request, headers);

        log.info("Appel à YOWYOB pour créer un profil Éditeur (ThirdParty) pour l'organisation: {}", organizationId);

        try {
            // On envoie la requête et on ne s'attend pas forcément à un corps de réponse complexe (d'où Void.class)
            // Si l'API renvoie un objet, on pourra changer Void.class par un DTO de réponse.
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            log.info("Profil Éditeur créé avec succès sur YOWYOB.");
        } catch (RestClientException e) {
            log.error("Erreur lors de la création du profil Éditeur sur YOWYOB: {}", e.getMessage());
            throw e;
        }
    }



    


    /**
     * Méthode utilitaire pour créer les en-têtes avec la double authentification.
     * @param userToken Le token JWT de l'utilisateur.
     * @return un objet HttpHeaders configuré.
     */
    private HttpHeaders createAuthenticatedHeaders(String userToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Authentification de l'utilisateur
        headers.setBearerAuth(userToken);

        // Identification de l'application
        headers.set("X-Application-ID", appId);
        headers.set("X-Public-Key", publicKey);

        return headers;
    }
    
    // Des méthodes pour createBusinessActor, etc., viendront ici.

    public void createBusinessActor(CreateBusinessActorRequest request, String userYowyobToken) throws RestClientException {
        String url = this.organizationServiceBaseUrl + "/business-actors";

        // Les en-têtes sont les mêmes que pour la création d'une organisation
        HttpHeaders headers = createAuthenticatedHeaders(userYowyobToken);
        
        HttpEntity<CreateBusinessActorRequest> entity = new HttpEntity<>(request, headers);

        // LOG D'INTENTION
        log.info("Appel à YOWYOB pour créer un Business Actor pour l'email: {}", request.getEmail());
        
        try {
            // On envoie la requête
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            // LOG DE SUCCÈS
            log.info("Business Actor créé avec succès sur YOWYOB.");
        } catch (RestClientException e) {
            // LOG D'ERREUR
            log.error("Erreur lors de la création du Business Actor sur YOWYOB: {}", e.getMessage());
            throw e;
        }
    }



}