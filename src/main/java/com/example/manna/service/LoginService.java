package com.example.manna.service;

import com.example.manna.util.Common;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
public class LoginService {
    @Value("${secret.google.auth.client_id}")
    private String client_id;

    @Value("${secret.google.auth.client_secret}")
    private String client_secret;

    final
    Common common;

    public LoginService(Common common) {
        this.common = common;
    }

    public HashMap<String, String> getTokenFromGoogle(String code, String redirect_url) {
        String reqURL = "https://oauth2.googleapis.com/token";
        HashMap<String, String> request_body = new HashMap<>();
        request_body.put("grant_type", "authorization_code");
        request_body.put("client_id", client_id);
        request_body.put("redirect_uri", redirect_url);
        request_body.put("client_secret", client_secret);
        request_body.put("code", code);
        String res = common.webClient.post()
                .uri(reqURL)
                .bodyValue(request_body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(res);

        assert res != null;
        JsonElement element = JsonParser.parseString(res);

        String access_token = element.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = element.getAsJsonObject().get("refresh_token").getAsString();

        HashMap<String, String> dto = new HashMap<>();
        dto.put("access_token", access_token);
        dto.put("refresh_token", refresh_token);

        return dto;
    }

    public String getUserInfoFromToken(String access_token, String refresh_token) {
        final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + access_token;
        try {
            return common.webClient.get()
                    .uri(GOOGLE_USER_INFO_URL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch(Exception e) {
            String res = makeAccessTokenFromRefreshToken(refresh_token);
            if (res.equals("expired")) {
                return "expired";
            } else {
                JsonElement element = JsonParser.parseString(res);
                String new_access_token = element.getAsJsonObject().get("access_token").getAsString();
                return getUserInfoFromToken(new_access_token, refresh_token);
            }
        }
    }

    public String makeAccessTokenFromRefreshToken(String refresh_token) {
        HashMap<String, String> request_body = new HashMap<>();
        request_body.put("grant_type", "refresh_token");
        request_body.put("refresh_token", refresh_token);
        request_body.put("client_id", client_id);
        request_body.put("client_secret", client_secret);
        try {
            return common.webClient.post()
                    .uri("https://accounts.google.com/o/oauth2/token")
                    .bodyValue(request_body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            return "expired";
        }
    }
}
