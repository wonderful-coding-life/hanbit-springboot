package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    // 홈 화면 - 누구나 접근 가능
    @GetMapping()
    public String getHome(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("userDetails = {}", userDetails);
        return "home";
    }

    // 상품 목록 화면 - 누구나 접근 가능
    @GetMapping("/product")
    public String getProduct() {
        return "product";
    }

    // 주문 화면 - 로그인한 회원만 접근 가능
    @GetMapping("/order")
    public String getOrder() {
        return "order";
    }

    // 회원 관리 화면 - 관리자만 접근 가능
    @GetMapping("/member")
    public String getMember() {
        return "member";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    // 로그아웃 화면
    @GetMapping("/logout")
    public String getLogout() {
        return "logout";
    }
}
