package com.example.manna.controller;

import com.example.manna.entity.UserDto;
import com.example.manna.repository.UserRepository;
import com.example.manna.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserRepository userRepository;
    final AuthService authService;
    @GetMapping("/token/getUserInfo")
    public UserDto UserInfo(HttpServletRequest request) {
        String serial_number = request.getAttribute("serial_number").toString();
        UserDto user = userRepository.findBySerialNumber(serial_number);
        return user;
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
}
