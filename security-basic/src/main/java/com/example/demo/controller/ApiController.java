package com.example.demo.controller;

import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {
    @GetMapping("/api/members")
    public List<Member> getMembers() {
        return List.of(
                Member.builder().name("윤서준").email("SeojunYoon@hanbit.co.kr").age(10).build(),
                Member.builder().name("윤광철").email("Kwangcheol@hanbit.co.kr").age(43).build()
        );
    }
}
