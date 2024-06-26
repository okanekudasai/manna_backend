package com.example.manna.controller;

import com.example.manna.entity.TestDto;
import com.example.manna.repository.TestRepository;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public List<TestDto> getAllTest() {
        System.out.println("옴");
        return testRepository.findAll();
    }

    @GetMapping("/getAllFirebaseUser")
    public List<String> getAllFirebaseUser() throws Exception {
        ArrayList<String> uid_list = new ArrayList<>();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                uid_list.add(user.getUid());
            }
            page = page.getNextPage();
        }
        return uid_list;
    }
}
