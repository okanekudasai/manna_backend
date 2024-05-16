package com.example.manna.service;

import com.example.manna.entity.feed.FeedDto;
import com.example.manna.entity.user.UserDto;
import com.example.manna.repository.FeedRepository;
import com.example.manna.repository.UserRepository;
import com.example.manna.util.JwtUtil;
import com.example.manna.util.TokenDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    final FeedRepository feedRepository;
    final UserRepository userRepository;
    @Transactional
    public List<FeedDto> getFeed() {
//        List<FeedDto> list = new ArrayList<>();
//        list.add(feedRepository.findById(1L).get());
        List<FeedDto> list = feedRepository.findAll();
//        System.out.println("----!!!------" + list.get(0).getWriter());
//        System.out.println();
//        System.out.println(list.get(0));
//        System.out.println(list.size());
        return list;
    }
    @Transactional
    public Page<FeedDto> getFeedPage(int num) {
        Pageable p = PageRequest.of(num, 6);
        return feedRepository.findAllByOrderByCreatedTimeDesc(p);
    }
}
