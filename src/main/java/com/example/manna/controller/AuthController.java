package com.example.manna.controller;

import com.example.manna.entity.user.UserDto;
import com.example.manna.repository.UserRepository;
import com.example.manna.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserRepository userRepository;
    final AuthService authService;

    static long anonymous_number = 1000;
    @GetMapping("/token/getUserInfo")
    public UserDto UserInfo(HttpServletRequest request) {
        String idx = request.getAttribute("idx").toString();
        Optional<UserDto> user = userRepository.findById(Long.parseLong(idx));
        UserDto user_entity = user.orElse(null);
        return user_entity;
    }

    @PostMapping("/token/changeTokenAge")
    public String changeTokenAge(@RequestParam boolean keep, HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.changeTokenAge(keep, request, response);
            return "OK";
        } catch (Exception e) {
            return "FAIL";
        }
    }

    @DeleteMapping("/deleteToken")
    public String deleteToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.deleteCookieList(request, response);
            return "OK";
        } catch(Exception e) {
            return "FAIL";
        }
    }

    @GetMapping("/getAnonymousNumber")
    public long getAnonymousNumber() {
        anonymous_number++;
        return anonymous_number;
    }
}
