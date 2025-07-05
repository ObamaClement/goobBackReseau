package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO pour la requête de connexion d'un utilisateur.
 */
@Data
public class LoginRequestDto {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire.")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    private String password;
}