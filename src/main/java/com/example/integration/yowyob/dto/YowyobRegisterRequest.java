package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * DTO représentant la requête à envoyer à l'API YOWYOB pour l'inscription.
 * Les noms de champs correspondent exactement à la structure JSON attendue par l'API externe.
 */
@Data
@Builder
public class YowyobRegisterRequest {

    private String username;
    private String email;
    private String password;

    // L'annotation @JsonProperty est utile si les conventions de nommage
    // diffèrent entre Java (camelCase) et le JSON (snake_case).
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private List<Authority> authorities;

    // Classe interne pour représenter la structure imbriquée de l'autorité
    @Data
    @Builder
    public static class Authority {
        private String name;
    }
}