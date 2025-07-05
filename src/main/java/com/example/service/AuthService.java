package com.example.service;

import com.example.dto.request.LoginRequestDto;
import com.example.dto.request.SignUpRequestDto;
import com.example.dto.response.AuthResponseDto;
import org.springframework.stereotype.Service;

/**
 * Interface pour le service d'authentification.
 * Définit les opérations d'inscription et de connexion.
 */
@Service
public interface AuthService {

    /**
     * Gère l'inscription d'un nouvel utilisateur.
     * Cette méthode orchestrera l'appel à l'API externe et la création
     * de l'utilisateur dans notre système local.
     *
     * @param signUpRequestDto Les données d'inscription.
     * @return Une réponse d'authentification contenant un token.
     */
    AuthResponseDto register(SignUpRequestDto signUpRequestDto);
    
    // Nous ajouterons la méthode de login plus tard
    // AuthResponseDto login(LoginRequestDto loginRequestDto);

    AuthResponseDto login(LoginRequestDto loginRequestDto);

}