package com.example.manna.service;

import com.example.manna.entity.UserDto;
import com.example.manna.repository.UserRepository;
import com.example.manna.util.JwtUtil;
import com.example.manna.util.TokenDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public TokenDto generateToken(String serial_number) {
        return jwtUtil.generateTokenDto(serial_number);
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


    public String checkUserExist(HashMap<String, String> user_info, HttpServletResponse response) {
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
            Cookie cookie = new Cookie("keep_login", "true");
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
            return "noobie";
        } else {
            System.out.println(user_info.get("email") + " 해당 유저는 있어요!");
            return "exist";
        }
    }

    public void deleteCookieList(HttpServletRequest request, HttpServletResponse response) {
        jwtUtil.deleteCookieList(response);
    }

    public void changeTokenAge(boolean keep, HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("keep_login")) continue;
            jwtUtil.addCookie(cookie.getName(), cookie.getValue(), response, keep);
        }
    }
}
