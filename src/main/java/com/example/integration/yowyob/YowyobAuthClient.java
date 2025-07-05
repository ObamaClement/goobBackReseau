package com.example.integration.yowyob;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.integration.yowyob.dto.YowyobLoginResponse;
import com.example.integration.yowyob.dto.YowyobRegisterRequest;
import com.example.integration.yowyob.dto.YowyobUserResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class YowyobAuthClient {

    private final RestTemplate restTemplate;
    private final String authServiceBaseUrl;

    @Data
    private static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
    }

    public YowyobAuthClient(RestTemplateBuilder builder, @Value("${yowyob.api.auth.base-url}") String authUrl) {
        this.restTemplate = builder.build();
        this.authServiceBaseUrl = authUrl;
    }

    public String getApplicationToken() {
        log.info("Demande d'un token d'application (client_credentials)...");
        String tokenUrl = this.authServiceBaseUrl + "/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("test-client", "secret");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "read write");
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, entity, TokenResponse.class);

        if (response.getBody() == null || response.getBody().getAccessToken() == null) {
            throw new IllegalStateException("Réponse invalide de l'API de token.");
        }
        return response.getBody().getAccessToken();
    }

    public YowyobUserResponse registerUser(YowyobRegisterRequest request, String appToken) {
        String registerUrl = this.authServiceBaseUrl + "/api/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(appToken);

        HttpEntity<YowyobRegisterRequest> entity = new HttpEntity<>(request, headers);

        log.info("Tentative d'inscription pour l'utilisateur: {} avec le token d'application.", request.getUsername());
        ResponseEntity<YowyobUserResponse> response = restTemplate.exchange(
                registerUrl, HttpMethod.POST, entity, YowyobUserResponse.class);
        return response.getBody();
    }

    public YowyobLoginResponse loginUser(String username, String password, String appToken) {
        String loginUrl = this.authServiceBaseUrl + "/api/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(appToken);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        log.info("Tentative de connexion YOWYOB pour : {}", username);
        ResponseEntity<YowyobLoginResponse> response = restTemplate.exchange(
                loginUrl, HttpMethod.POST, entity, YowyobLoginResponse.class);
        return response.getBody();
    }
}