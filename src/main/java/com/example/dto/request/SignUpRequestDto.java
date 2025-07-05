package com.example.dto.request;

import com.example.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO pour la requête d'inscription reçue par notre propre API.
 */
@Data
public class SignUpRequestDto {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire.")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "Le format de l'email est invalide.")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères.")
    private String password;
    
    @NotBlank(message = "Le prénom est obligatoire.")
    private String firstName;
    
    @NotBlank(message = "Le nom de famille est obligatoire.")
    private String lastName;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire.")
    private String phoneNumber;

    @NotNull(message = "Le rôle de l'utilisateur est obligatoire (ex: ROLE_EDITOR, ROLE_AFFILIATE).")
    private UserRole role;

    // Champs optionnels qui dépendent du rôle
    private String businessName; // Pour les éditeurs
    private Long followers;      // Pour les affiliés
}