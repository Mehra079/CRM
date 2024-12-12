package com.example.authentication.util;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KeycloakTokenRevocationService {

    private final WebClient webClient;

    public KeycloakTokenRevocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://keycloak:8080").build();
    }

    public void revokeToken(String token) {
        String clientId = "authentication-service"; // Replace with actual client ID
        String clientSecret = "client-secret"; // Retrieve securely from properties

        webClient.post()
                .uri("/realms/my_realm/protocol/openid-connect/revoke")
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
                .bodyValue("token=" + token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
