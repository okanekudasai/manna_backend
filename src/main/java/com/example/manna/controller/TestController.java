package com.example.manna.controller;

import com.example.manna.entity.Test;
import com.example.manna.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestRepository testRepository;

    @GetMapping("/hello")
    String test() {
        System.out.println("hello world");
        return "hello world!!!";
    }

    @GetMapping("/getAllTest")
    public List<Test> getAllTest() {
        System.out.println("옴");
        return testRepository.findAll();
    }
}
