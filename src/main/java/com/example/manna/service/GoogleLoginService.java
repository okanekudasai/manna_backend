package com.example.manna.service;

import com.example.manna.util.CommonUtil;
import com.example.manna.util.JwtUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    @Value("${secret.auth.google.client_id}")
    private String client_id;

    @Value("${secret.auth.google.client_secret}")
    private String client_secret;

    private final CommonUtil commonUtil;
    private final JwtUtil jwtUtil;

    public String getIdTokenFromGoogle(String code, String redirect_url) {
        String reqURL = "https://oauth2.googleapis.com/token";
        HashMap<String, String> request_body = new HashMap<>();
        request_body.put("grant_type", "authorization_code");
        request_body.put("client_id", client_id);
        request_body.put("redirect_uri", redirect_url);
        request_body.put("client_secret", client_secret);
        request_body.put("code", code);
        String res = commonUtil.webClient.post()
                .uri(reqURL)
                .bodyValue(request_body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assert res != null;
        JsonElement element = JsonParser.parseString(res);
        String id_token = element.getAsJsonObject().get("id_token").getAsString();
        return id_token;
    }

    public HashMap<String, String> getUserInfoFromIdToken(String id_token) {
        HashMap<String, String> claim = jwtUtil.parseIdToken(id_token);
        return claim;
    }
}
