package com.example.manna.controller;

import com.example.manna.entity.feed.FeedDto;
import com.example.manna.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {
    final FeedService feedService;
    @GetMapping("/getFeed")
    public List<FeedDto> getFeed() {
        return feedService.getFeed();
    }

    @GetMapping("/getFeedPage/{num}")
    Page<FeedDto> getFeedPage(@PathVariable int num) {
        return feedService.getFeedPage(num);
    }
}
