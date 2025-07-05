package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour la requête de création d'une Organisation (Éditeur)
 * auprès de l'API YOWYOB organization-service.
 */
@Data
@Builder
public class CreateOrganizationRequest {

    // On suppose qu'un Éditeur est un "PROVIDER" (fournisseur de produits/offres)
    // Le type "11" pourrait correspondre à "Provider" dans leur système.
    @JsonProperty("type")
    private final String type = "11"; 
    
    // Le nom commercial de l'éditeur
    @JsonProperty("name")
    private String name;

    // Le nom long ou légal
    @JsonProperty("long_name")
    private String longName;

    // Une description/biographie de l'entreprise
    @JsonProperty("description")
    private String description;

    // URL du logo
    @JsonProperty("logo")
    private String logo;

    // Numéro de taxe, SIRET, etc. (optionnel mais bon à avoir)
    @JsonProperty("tax_number")
    private String taxNumber;
    
    // Et d'autres champs si nécessaires...
}