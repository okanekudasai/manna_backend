package com.example.manna.util;

import com.example.manna.entity.TestDto;
import com.example.manna.repository.TestRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Transactional
public class DummyDataLoader  {
    final TestRepository testRepository;
    @PostConstruct
    public void init() {
        TestDto user1 = TestDto.builder()
                .username("aaaa")
                .email("aaa@aaa.aaa")
                .password("aaaaaas")
                .build();
        testRepository.save(user1);
    }
}
