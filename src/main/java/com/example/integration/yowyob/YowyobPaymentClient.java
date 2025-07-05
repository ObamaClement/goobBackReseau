package com.example.integration.yowyob;

import com.example.integration.yowyob.dto.PayInRequestDto;
import com.example.integration.yowyob.dto.PayInResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Client HTTP pour communiquer avec le microservice de paiement de YOWYOB.
 */
@Component
@Slf4j
public class YowyobPaymentClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceBaseUrl;

    // --- On aura besoin du token d'application, comme pour l'autre client ---
    private final YowyobAuthClient yowyobAuthClient;

    public YowyobPaymentClient(RestTemplateBuilder builder,
                               @Value("${yowyob.api.payment.base-url}") String paymentUrl,
                               YowyobAuthClient authClient) {
        this.restTemplate = builder.build();
        this.paymentServiceBaseUrl = paymentUrl;
        this.yowyobAuthClient = authClient;
    }
    
    /**
     * Initialise une transaction de paiement (Pay-in) auprès de l'API YOWYOB.
     *
     * @param request Le DTO contenant tous les détails du paiement.
     * @return La réponse structurée de l'API de paiement.
     * @throws RestClientException Si l'appel à l'API échoue.
     */
    public PayInResponseDto initiatePayIn(PayInRequestDto request) throws RestClientException {
        // 1. Obtenir un token d'application valide pour autoriser notre requête
        String appToken = yowyobAuthClient.getApplicationToken();
        
        String payInUrl = this.paymentServiceBaseUrl + "/payin";

        // 2. Préparer les en-têtes de la requête, y compris le Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(appToken);

        // 3. Créer l'entité de la requête avec le corps et les en-têtes
        HttpEntity<PayInRequestDto> entity = new HttpEntity<>(request, headers);

        log.info("Initiation d'un paiement (Pay-in) pour la référence : {}", request.getTransactionReference());
        
        try {
            // 4. Envoyer la requête POST
            ResponseEntity<PayInResponseDto> response = restTemplate.exchange(
                    payInUrl,
                    HttpMethod.POST,
                    entity,
                    PayInResponseDto.class
            );

            log.info("Réponse reçue de l'API Pay-in avec le statut : {}", response.getStatusCode());
            return response.getBody();

        } catch (RestClientException e) {
            log.error("Erreur lors de l'appel à l'API Pay-in : {}", e.getMessage(), e);
            throw e; // Propage l'exception pour que le service métier la gère
        }
    }
}