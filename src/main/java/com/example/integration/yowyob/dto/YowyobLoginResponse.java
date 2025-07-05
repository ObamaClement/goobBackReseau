package com.example.integration.yowyob.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * DTO pour désérialiser la réponse complète de l'endpoint /api/login de YOWYOB.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YowyobLoginResponse {

    @JsonProperty("access_token")
    private AccessTokenInfo accessToken;

    private UserInfo user;
    private List<String> roles;
    private List<String> permissions;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessTokenInfo {
        private String token;
        private String type;
        @JsonProperty("expire_in")
        private int expiresIn;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        private UUID id;
        private String username;
        private String email;
    }
}