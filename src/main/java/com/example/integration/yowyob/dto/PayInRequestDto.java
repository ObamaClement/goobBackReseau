package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO pour la requête de paiement (Pay-in) envoyée à l'API YOWYOB.
 * Les noms des champs correspondent exactement à la documentation de l'API externe.
 */
@Data
@Builder
public class PayInRequestDto {

    @JsonProperty("transaction_amount")
    private BigDecimal transactionAmount;

    @JsonProperty("transaction_currency")
    private final String transactionCurrency = "XAF"; // Devise fixée selon la documentation

    @JsonProperty("transaction_method")
    private final String transactionMethod = "MOBILE"; // Méthode de paiement fixée

    // Notre référence interne (ex: l'ID de notre Transaction locale)
    @JsonProperty("transaction_reference")
    private String transactionReference;

    @JsonProperty("payer_reference")
    private String payerReference; // ID de l'acheteur s'il est connu

    @JsonProperty("payer_name")
    private String payerName;

    @JsonProperty("payer_phone_number")
    private String payerPhoneNumber;
    
    @JsonProperty("payer_lang")
    private final String payerLang = "fr"; // Langue fixée

    @JsonProperty("payer_email")
    private String payerEmail;

    // Informations sur le service/produit acheté
    @JsonProperty("service_reference")
    private String serviceReference; // Ex: L'ID de notre produit
    
    @JsonProperty("service_name")
    private String serviceName;
    
    @JsonProperty("service_description")
    private String serviceDescription;

    @JsonProperty("service_quantity")
    private int serviceQuantity = 1; // On suppose 1 par transaction pour l'instant
}