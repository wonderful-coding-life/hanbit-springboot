package com.example.demo.controller;

import com.example.demo.model.Member;
import com.example.demo.model.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @GetMapping("/api/members")
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
