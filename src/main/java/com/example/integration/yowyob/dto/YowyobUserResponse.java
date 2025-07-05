package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO représentant la réponse reçue de l'API YOWYOB après la création
 * ou la récupération d'un utilisateur.
 * Les noms de champs correspondent au JSON renvoyé par l'API externe.
 */
@Data
// Ignore toutes les propriétés inconnues que l'API pourrait renvoyer et que nous ne gérons pas.
@JsonIgnoreProperties(ignoreUnknown = true)
public class YowyobUserResponse {

    private UUID id;

    private String username;

    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}