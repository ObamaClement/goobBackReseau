package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBusinessActorRequest {
    
    // On mappe les informations de base de notre utilisateur
    private String email;
    
    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;

    // Champs par défaut pour un affilié
    @JsonProperty("is_individual")
    private final boolean isIndividual = true; // Un affilié est typiquement un individu

    private String profession = "Digital Content Creator"; // Profession par défaut

    private String biography = "Affiliate partner of TransAffil Platform"; // Bio par défaut
    
    // "PROVIDER" car l'affilié "fournit" un service de promotion
    private String type = "PROVIDER";
}