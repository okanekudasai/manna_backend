package com.example.manna.controller;

import com.example.manna.handler.ChatRoom;
import com.example.manna.handler.SocketHandler;
import com.example.manna.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    final SocketHandler socketHandler;
    final JwtUtil jwtUtil;

    @GetMapping("/getChatRoomSet")
    HashSet<ChatRoom> getChatRoomSet() {
        return socketHandler.getChatRoomSet();
    }

    @GetMapping("/getToken/{nickname}")
    void getToken(@PathVariable("nickname") String nickname, HttpServletResponse response) {
        String token = jwtUtil.generateToken(nickname);
        jwtUtil.addCookie(token, response);
    }

    @GetMapping("/confirmToken")
    String confirmToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String access_token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    access_token = cookie.getValue();
                }
            }
        }
        Claims claims = jwtUtil.parseClaims(access_token);
        boolean flag = false;
        if(claims == null) {
            System.out.println("실패");
            jwtUtil.deleteCookie("access_token", response);
        } else {
            flag = true;
            System.out.println("성공");
            jwtUtil.deleteCookie("access_token", response);
        }
        return String.valueOf(flag);
    }
    @DeleteMapping("/deleteToken")
    void deleteToken(HttpServletResponse response) {
        jwtUtil.deleteCookie("access_token", response);
    }


}
