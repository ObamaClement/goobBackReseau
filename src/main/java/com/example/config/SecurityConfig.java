package com.example.config;

import com.example.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // --- MODIFICATION CLÉ ICI ---
                .requestMatchers("/api/v1/auth/**").permitAll() // L'authentification est publique
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/v3/api-docs/**",     // Pour le fichier de spec OpenAPI JSON
                    "/swagger-ui/**",       // Pour l'interface web de Swagger
                    "/swagger-ui.html"      // Redirection potentielle
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll() // La LECTURE des produits est publique
                .requestMatchers(HttpMethod.GET, "/api/v1/editors/**").permitAll() // La LECTURE des éditeurs/leurs produits est publique
                .anyRequest().authenticated() // TOUT le reste nécessite une authentification
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}