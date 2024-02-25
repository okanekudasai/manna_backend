package com.example.manna.controller;


import com.example.manna.service.GoogleLoginService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth/google")
@CrossOrigin(origins = {"http://localhost:5173", "https://mannayo.duckdns.org"})
public class GoogleLoginController {
    final GoogleLoginService googleLoginService;

    public GoogleLoginController(GoogleLoginService googleLoginService) {
        this.googleLoginService = googleLoginService;
    }

    @PostMapping("/takeUserInfoWithCode")
    HashMap<String, String> takeCodeFromClient(@RequestParam String code, @RequestParam String redirect_url) {
        String id_token = googleLoginService.getIdTokenFromGoogle(code, redirect_url);
        HashMap<String, String> user_info = googleLoginService.getUserInfoFromIdToken(id_token);
        return null;
    }
//    @PostMapping("/getUserInfoFromToken")
//    String getUserInfoFromToken(@RequestParam String access_token, @RequestParam String refresh_token) {
//        return loginService.getUserInfoFromToken(access_token, refresh_token);
//    }

}
