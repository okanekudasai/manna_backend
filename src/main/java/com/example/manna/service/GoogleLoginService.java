package com.example.manna.service;

import com.example.manna.util.Common;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GoogleLoginService {
    @Value("${secret.google.auth.client_id}")
    private String client_id;

    @Value("${secret.google.auth.client_secret}")
    private String client_secret;

    final
    Common common;

    public GoogleLoginService(Common common) {
        this.common = common;
    }

    public String getIdTokenFromGoogle(String code, String redirect_url) {
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

        assert res != null;
        JsonElement element = JsonParser.parseString(res);
        String id_token = element.getAsJsonObject().get("id_token").getAsString();

        System.out.println("id_token: " + id_token);

        return id_token;
    }

    public HashMap<String, String> getUserInfoFromIdToken(String idToken) {
        return null;
    }
}
