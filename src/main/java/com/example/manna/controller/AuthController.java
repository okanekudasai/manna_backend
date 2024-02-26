package com.example.manna.controller;

import com.example.manna.entity.UserDto;
import com.example.manna.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserRepository userRepository;
    @GetMapping("/token/getUserInfo")
    public UserDto UserInfo(HttpServletRequest request) {
        String serial_number = request.getAttribute("serial_number").toString();
        UserDto user = userRepository.findBySerialNumber(serial_number);
        return user;
    }
}
