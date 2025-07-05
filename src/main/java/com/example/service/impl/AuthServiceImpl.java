package com.example.service.impl;

import com.example.dto.request.LoginRequestDto;
import com.example.dto.request.SignUpRequestDto;
import com.example.dto.response.AuthResponseDto;
import com.example.entity.User;
import com.example.entity.UserRole; // <-- AJOUTER L'IMPORT
import com.example.integration.yowyob.YowyobAuthClient;
import com.example.integration.yowyob.dto.YowyobLoginResponse;
import com.example.integration.yowyob.dto.YowyobRegisterRequest;
import com.example.integration.yowyob.dto.YowyobUserResponse;
import com.example.repository.UserRepository;
import com.example.security.JwtTokenProvider;
import com.example.service.AuthService;
import com.example.service.OrganizationService; // <-- AJOUTER L'IMPORT
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final YowyobAuthClient yowyobAuthClient;
    private final OrganizationService organizationService; // <-- NOUVELLE DÉPENDANCE
    private final JwtTokenProvider jwtTokenProvider;
    
    // Le AuthenticationManager n'est plus nécessaire si on délègue 100% de la validation du mdp à YOWYOB.
    // private final AuthenticationManager authenticationManager; 

    @Override
    @Transactional
    public AuthResponseDto register(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByUsername(signUpRequestDto.getUsername()) || userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new IllegalArgumentException("Le nom d'utilisateur ou l'email est déjà pris.");
        }
        
        String appToken = yowyobAuthClient.getApplicationToken();
        log.debug("Token d'application pour register : {}", appToken);

        YowyobRegisterRequest yowyobRequest = YowyobRegisterRequest.builder()
                .username(signUpRequestDto.getUsername())
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .firstName(signUpRequestDto.getFirstName())
                .lastName(signUpRequestDto.getLastName())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .authorities(Collections.emptyList())
                .build();
        
        YowyobUserResponse yowyobUser;
        try {
            yowyobUser = yowyobAuthClient.registerUser(yowyobRequest, appToken);
        } catch (RestClientException e) {
            throw new IllegalStateException("Le service externe a refusé l'inscription d'identité.");
        }

        // Création de l'entité locale
        User user = User.builder()
                .id(yowyobUser.getId())
                .username(yowyobUser.getUsername())
                .email(yowyobUser.getEmail())
                .password("EXTERNALLY_MANAGED")
                .role(signUpRequestDto.getRole())
                .businessName(signUpRequestDto.getBusinessName())
                .bio(null) // A compléter plus tard si nécessaire
                .build();
        
        // --- NOUVELLE LOGIQUE CONDITIONNELLE ---
        if (user.getRole() == UserRole.ROLE_EDITOR) {
            // Pour se connecter et obtenir le token de l'utilisateur qui vient d'être créé
            YowyobLoginResponse loginResponse = yowyobAuthClient.loginUser(
                    user.getUsername(), signUpRequestDto.getPassword(), appToken);
            String yowyobUserToken = loginResponse.getAccessToken().getToken();
            
            // On utilise ce token pour créer le profil professionnel
            organizationService.createEditorProfileOnExternalApi(user, yowyobUserToken);
        } else if (user.getRole() == UserRole.ROLE_EDITOR) {
            // ... (logique existante pour l'éditeur)
        } else if (user.getRole() == UserRole.ROLE_AFFILIATE) {
            // ---- MODIFICATION ICI ----
            log.info("L'utilisateur est un affilié. Tentative de création du profil Business Actor...");
            // On a besoin du token utilisateur pour cette opération aussi.
            YowyobLoginResponse loginResponse = yowyobAuthClient.loginUser(
                    user.getUsername(), signUpRequestDto.getPassword(), appToken);
            String yowyobUserToken = loginResponse.getAccessToken().getToken();

            // On utilise ce token pour créer le profil Business Actor
            organizationService.createAffiliateProfileOnExternalApi(user, yowyobUserToken);
        }
        User savedUser = userRepository.save(user);
        String transaffilToken = jwtTokenProvider.generateToken(savedUser);

        return AuthResponseDto.builder()
                .token(transaffilToken)
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        // --- NOUVELLE LOGIQUE COMPLÈTE ---

        // Étape 1 : Obtenir le Token d'Application
        String appToken = yowyobAuthClient.getApplicationToken();
        log.info("Étape 1/4 : Token d'Application obtenu.");

        // Étape 2 : Utiliser le Token d'Application pour obtenir le Token Utilisateur YOWYOB
        YowyobLoginResponse yowyobLoginResponse;
        try {
            yowyobLoginResponse = yowyobAuthClient.loginUser(
                loginRequestDto.getUsername(), 
                loginRequestDto.getPassword(), 
                appToken
            );
            log.info("Étape 2/4 : Token Utilisateur YOWYOB obtenu pour {}.", loginRequestDto.getUsername());
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Identifiants invalides auprès du service externe.", e);
        }

        // Étape 3 : Récupérer notre utilisateur local pour connaître son rôle TransAffil
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur authentifié par YOWYOB mais non trouvé localement: " + loginRequestDto.getUsername()));
        log.info("Étape 3/4 : Utilisateur local {} trouvé avec le rôle {}.", user.getUsername(), user.getRole());
        
        // Étape 4 : Générer notre propre token de session TransAffil
        String transaffilToken = jwtTokenProvider.generateToken(user);
        log.info("Étape 4/4 : Token de session TransAffil généré.");

        // Construction de la réponse finale, incluant les tokens de débogage
        return AuthResponseDto.builder()
                .token(transaffilToken) // Le token pour le frontend
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .debugTokens(AuthResponseDto.DebugTokens.builder() // Infos pour nous aider
                        .appToken(appToken)
                        .yowyobUserToken(yowyobLoginResponse.getAccessToken().getToken())
                        .build())
                .build();
    }
}