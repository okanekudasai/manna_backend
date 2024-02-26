package com.example.manna.controller;


import com.example.manna.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth/google")
@CrossOrigin(origins = {"http://localhost:5173", "https://mannayo.duckdns.org"})
@RequiredArgsConstructor
public class GoogleLoginController {
    final GoogleLoginService googleLoginService;

    @PostMapping("/takeUserInfoWithCode")
    HashMap<String, String> takeUserInfoWithCode(@RequestParam String code, @RequestParam String redirect_url) {
        String id_token = googleLoginService.getIdTokenFromGoogle(code, redirect_url);
        HashMap<String, String> user_info = googleLoginService.getUserInfoFromIdToken(id_token);

        // 유저 정보를 db에서 뒤져봐서 있는지 없는지 파악해요
        return user_info;
    }
//    @PostMapping("/getUserInfoFromToken")
//    String getUserInfoFromToken(@RequestParam String access_token, @RequestParam String refresh_token) {
//        return loginService.getUserInfoFromToken(access_token, refresh_token);
//    }

}
