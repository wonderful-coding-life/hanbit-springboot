package com.example.demo.controller;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;

@RestController
@Slf4j
public class LectureMemberController {
    @Autowired
    private MemberRepository memberRepository;

    /*
     * (1) Client --(HTTP Packet)--> Tomcat
     * (2) Tomcat --(HttpServletRequest)--> Dispatcher Servlet
     * (3) Dispatcher Servlet --(HttpServletRequest)--> Handler Adapter
     * (4) Handler Adapter --(Member member)--> Controller method
     */

    @PostMapping("/members")
    public Member postMembers(/* @RequestBody Member member, */HttpServletRequest request) throws IOException {
        log.info("URL: {}", request.getRequestURL());
        log.info("URI: {}", request.getRequestURI());
        log.info("Method: {}", request.getMethod());
        log.info("Query: {}", request.getQueryString());
        log.info("Header Content-Type: {}", request.getHeader("Content-Type"));
        BufferedReader reader = request.getReader();
        String line;
        log.info("Request Body");
        while ((line = reader.readLine()) != null) {
            log.info("{}", line);
        }
        return null; //memberRepository.save(member);
    }
}
