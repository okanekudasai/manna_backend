package com.example.manna.service;

import com.example.manna.entity.UserDto;
import com.example.manna.repository.UserRepository;
import com.example.manna.util.CommonUtil;
import com.example.manna.util.JwtUtil;
import com.example.manna.util.TokenDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    @Value("${secret.auth.google.client_id}")
    private String clientId;

    @Value("${secret.auth.google.client_secret}")
    private String clientSecret;

    private final CommonUtil commonUtil;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public TokenDto generateToken(String serial_number) {
        return jwtUtil.generateTokenDto(serial_number);
    }

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

            String idToken = element.getAsJsonObject().get("id_token").getAsString();
            return idToken;
        } catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * 토큰으로 유저 정보를 가져와요
     * @param id_token
     * @return
     */
    public HashMap<String, String> getUserInfoFromIdToken(String id_token, String host) {
        HashMap<String, String> claim = jwtUtil.parseIdToken(id_token, host);
        return claim;
    }

    public void checkUserExist(HashMap<String, String> user_info) {
        if (!userRepository.existsBySerialNumber(user_info.get("serial_number"))) {
            System.out.println(user_info.get("email") + " 해당 유저는 없어요!");
            UserDto dto = UserDto.builder()
                    .serialNumber(user_info.get("serial_number"))
                    .email(user_info.get("email"))
                    .name(user_info.get("name"))
                    .profile_url(user_info.get("profile_url"))
                    .nation(user_info.get("locale"))
                    .deleted(false)
                    .createdDate(LocalDateTime.now())
                    .build();
            userRepository.save(dto);
        } else {
            System.out.println(user_info.get("email") + " 해당 유저는 있어요!");
        }
    }

    /**
     * 시리얼 넘버로 db에 해당 유저가 있는지 확인해요
     */
}
