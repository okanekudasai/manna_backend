package com.example.manna.service;

import com.example.manna.util.CommonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    @Value("${secret.auth.google.client_id}")
    private String clientId;

    @Value("${secret.auth.google.client_secret}")
    private String clientSecret;

    private final CommonUtil commonUtil;

    public String getIdTokenFromGoogle(String code, String redirectUrl) {
        String reqURL = "https://oauth2.googleapis.com/token";
        HashMap<String, String> request_body = new HashMap<>();
        request_body.put("grantType", "authorization_code");
        request_body.put("clientId", clientId);
        request_body.put("redirectUri", redirectUrl);
        request_body.put("clientSecret", clientSecret);
        request_body.put("code", code);
        try {
            String res = commonUtil.webClient.post()
                    .uri(reqURL)
                    .bodyValue(request_body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            assert res != null;
            JsonElement element = JsonParser.parseString(res);

            return element.getAsJsonObject().get("id_token").getAsString();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
