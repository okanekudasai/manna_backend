package com.example.manna.configurer;

import com.example.manna.util.JwtUtil;
import com.example.manna.util.TokenDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    final JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("token인터셉터 실행!");
        Cookie[] cookies = request.getCookies();
        String access_token = null;
        String refresh_token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
//                System.out.println(cookie.getName());
                if ("access_token".equals(cookie.getName())) {
                    access_token = cookie.getValue();
                } else if ("refresh_token".equals(cookie.getName())) {
                    refresh_token = cookie.getValue();
                }
            }
        }

        if (access_token == null) return false;
        else {
            Claims access_claims = isValidToken(access_token);
            if (access_claims == null) {
                Claims refresh_claims = isValidToken(refresh_token);
                if (refresh_claims == null) {
                    return false;
                } else {
                    String serial_number = refresh_claims.getSubject();
                    TokenDto token = jwtUtil.generateTokenDto(serial_number);
                    jwtUtil.addCookieList(token, response, true);
                    request.setAttribute("serial_number", serial_number);
                    return true;
                }
            } else {
                request.setAttribute("serial_number", access_claims.getSubject());
                return true;
            }
        }
    }

    private Claims isValidToken(String token) {
        return jwtUtil.parseClaims(token);
    }
}
