package com.example.manna.controller;


import com.example.manna.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "https://mannayo.duckdns.org"})
public class LoginController {
    final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/takeCodeFromClient")
    HashMap<String, String> takeCodeFromClient(@RequestParam String code, @RequestParam String redirect_url) {
        System.out.println("code: " + code);
        System.out.println("redirect: " + redirect_url);
        return loginService.getTokenFromGoogle(code, redirect_url);
    }
    @PostMapping("/getUserInfoFromToken")
    String getUserInfoFromToken(@RequestParam String access_token, @RequestParam String refresh_token) {
        return loginService.getUserInfoFromToken(access_token, refresh_token);
    }

}
