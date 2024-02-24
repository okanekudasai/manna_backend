package com.example.manna.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
public class LoginService {
    @Value("${secret.google.auth.client_id}")
    private String client_id;

    @Value("${secret.google.auth.client_secret}")
    private String client_secret;

    ObjectMapper mapper = new ObjectMapper();
    JsonParser parser = new JsonParser();

    public HashMap<String, String> getAccessTokenFromGoogle(String code, String redirect_url) {
        String reqURL = "https://oauth2.googleapis.com/token";
        WebClient webClient = WebClient.builder().build();
        HashMap<String, String> request_body = new HashMap<>();
        request_body.put("grant_type", "authorization_code");
        request_body.put("client_id", client_id);
        request_body.put("redirect_uri", redirect_url);
        request_body.put("client_secret", client_secret);
        request_body.put("code", code);
        String res = webClient.post()
                .uri(reqURL)
                .bodyValue(request_body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(res);

        String access_token = element.getAsJsonObject().get("access_token").getAsString();
        String id_token = element.getAsJsonObject().get("id_token").getAsString();

        HashMap<String, String> dto = new HashMap<>();
        dto.put("access_token", access_token);
        dto.put("id_token", id_token);
        return dto;
    }
}
