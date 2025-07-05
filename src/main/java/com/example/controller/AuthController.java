package com.example.controller;

import com.example.dto.request.LoginRequestDto;
import com.example.dto.request.SignUpRequestDto;
import com.example.dto.response.AuthResponseDto;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller pour les endpoints d'authentification (inscription, connexion).
 * Tous les endpoints ici sont publics car définis dans SecurityConfig.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        AuthResponseDto response = authService.register(signUpRequestDto);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint pour la connexion d'un utilisateur existant.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }
}