package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDto {

    private String token; // Token de session TransAffil
    private final String tokenType = "Bearer";
    private UUID userId;
    private String username;
    private String role;
    
    // Champs de Débogage pour voir tous les tokens
    private DebugTokens debugTokens;
    
    @Data
    @Builder
    public static class DebugTokens {
        private String appToken;
        private String yowyobUserToken;
    }
}