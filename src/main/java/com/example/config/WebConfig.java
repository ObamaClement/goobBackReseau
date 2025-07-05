package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Web globale de l'application, notamment pour le CORS.
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Applique le CORS à toutes les routes de votre API
                        .allowedOrigins("*") // Autorise TOUTES les origines. Idéal pour le dev, mais à restreindre en production.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                        .allowedHeaders("*") // Autorise tous les en-têtes (comme Authorization)
                        .allowCredentials(false); // Mettre à false si allowedOrigins est '*'
            }
        };
    }
}