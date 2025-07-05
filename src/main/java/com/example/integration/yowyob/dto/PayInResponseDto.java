package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

/**
 * DTO pour mapper la réponse de l'endpoint /payin de l'API YOWYOB.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Très important pour ignorer les autres champs (status, ok, errors...)
public class PayInResponseDto {

    // On s'intéresse principalement à l'objet 'data' imbriqué
    @JsonProperty("data")
    private PayInData data;

    // Classe interne pour mapper l'objet "data"
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayInData {

        private String message;

        @JsonProperty("status_code")
        private int statusCode;

        @JsonProperty("transaction_code")
        private UUID transactionCode; // C'est l'ID de transaction de YOWYOB

        @JsonProperty("transaction_status")
        private String transactionStatus; // Ex: "CREATED", "PENDING"
    }
}