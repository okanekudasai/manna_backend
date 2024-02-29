package com.example.manna.util;

import com.example.manna.entity.UserDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtUtil {
    public static final String BEARER_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.key}")
    private String secret_key;

    @Value("${jwt.expiration.access-token}")
    private long access_token_expiration;

    @Value("${jwt.expiration.refresh-token}")
    private long refresh_token_expiration;

    private Key key;

    @PostConstruct
    public void init() {
        String base64EncodedSecretKey = encodeBase64SecretKey(this.secret_key);
        this.key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }

    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(String serial_number) {
        long now = (new Date()).getTime();

        String access_token = Jwts.builder()
                .setSubject(serial_number)
                .setExpiration(new Date(now + access_token_expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refresh_token = Jwts.builder()
                .setSubject(serial_number)
                .setExpiration(new Date(now + refresh_token_expiration * 1000))
                .signWith(key)
                .compact();

        return TokenDto.builder()
//                .grantType(BEARER_TYPE)
//                .authorizationType(AUTHORIZATION_HEADER)
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch(Exception e) {
            return null;
        }
    }

    public HashMap<String, String> parseIdToken(String token, String host) {
        try {
            byte[] decode = Base64.getUrlDecoder().decode(token.split("\\.")[1]);
            String info = new String(decode, StandardCharsets.UTF_8);
            JsonElement element = JsonParser.parseString(info);
            String serial_number = host + "_" + element.getAsJsonObject().get("sub").getAsString();
            String email = element.getAsJsonObject().get("email").getAsString();
            String name = element.getAsJsonObject().get("name").getAsString();
            String profile_url = element.getAsJsonObject().get("picture").getAsString();
            String locale = element.getAsJsonObject().get("locale").getAsString();

            HashMap<String, String> res = new HashMap<>();
            res.put("serial_number", serial_number);
            res.put("email", email);
            res.put("name", name);
            res.put("profile_url", profile_url);
            res.put("locale", locale);
            return res;
        } catch(ExpiredJwtException e) {
            return null;
        }
    }

    public void addCookie(String name, String value, HttpServletResponse response, boolean keep) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        if (keep) {
            cookie.setMaxAge(Integer.MAX_VALUE);
        }
        response.addCookie(cookie);
    }

    public void addCookieList(TokenDto token, HttpServletResponse response, boolean keep) {
        addCookie("access_token", token.getAccessToken(), response, keep);
        addCookie("refresh_token", token.getRefreshToken(), response, keep);
//        addCookie("grant_type", token.getGrantType(), response, keep);
//        addCookie("authorization_type", token.getAuthorizationType(), response, keep);
    }

    public void deleteCookie(String name, HttpServletResponse response) {
//        System.out.println(name);
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    public void deleteCookieList(HttpServletResponse response) {
        deleteCookie("access_token", response);
        deleteCookie("refresh_token", response);
//        deleteCookie("grant_type", response);
//        deleteCookie("authorization_type",  response);
    }

    public void keep_login_true(HttpServletResponse response) {
        Cookie cookie = new Cookie("keep_login", "true");
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
    }
}
