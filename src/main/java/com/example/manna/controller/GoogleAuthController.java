package com.example.manna.controller;


import com.example.manna.entity.UserDto;
import com.example.manna.service.AuthService;
import com.example.manna.service.GoogleAuthService;
import com.example.manna.util.JwtUtil;
import com.example.manna.util.TokenDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {
    final AuthService authService;
    final GoogleAuthService googleAuthService;
    final JwtUtil jwtUtil;

    @PostMapping("/takeUserInfoWithCode")
    UserDto takeUserInfoWithCode(@RequestParam String code, @RequestParam String redirect_url, HttpServletResponse response) {

        // code를 가지고 구글에서 내정보를 가지고 오기위해 필요한 id_token을 가져와요
        String id_token = googleAuthService.getIdTokenFromGoogle(code, redirect_url);
        if (id_token == null) {
            return null;
        }

        // id_token을 사용해서 유저 정보를 가져와요
        HashMap<String, String> user_info = authService.getUserInfoFromIdToken(id_token, "google");

        // 유저정보가 이미 등록되있는지 여부를 살펴봐요. 등록되있지 않다면 등록해요
        UserDto user_entity = authService.checkUserExist(user_info);

        // 토큰을 만들어요
//        TokenDto token = authService.generateToken(user_info.get("serial_number"));
        TokenDto token = authService.generateToken(String.valueOf(user_entity.getIdx()));

        // 코드로 로그인 하면 꼭 keeplogin을 true로 해줄게요
        jwtUtil.keep_login_true(response);

        // 만든 토큰을 쿠키에 저장해요
        jwtUtil.addCookieList(token, response, true);

        // 토큰을 반환해줘요
        return user_entity;
    }

}
