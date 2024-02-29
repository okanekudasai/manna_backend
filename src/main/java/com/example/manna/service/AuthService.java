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


    public TokenDto generateToken(String idx) {
        return jwtUtil.generateTokenDto(idx);
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


    public UserDto checkUserExist(HashMap<String, String> user_info) {
        UserDto user = userRepository.findBySerialNumberAndDeleted(user_info.get("serial_number"), Boolean.parseBoolean(user_info.get("deleted")));
//        if (!userRepository.existsBySerialNumberAndDeleted(user_info.get("serial_number"), Boolean.parseBoolean(user_info.get("deleted")))) {
        if (user == null) {
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

            return userRepository.save(dto);
        } else {
            System.out.println(user_info.get("email") + " 해당 유저는 있어요!");
            return user;
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
