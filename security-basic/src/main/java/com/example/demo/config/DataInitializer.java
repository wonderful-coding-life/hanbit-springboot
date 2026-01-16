package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (memberRepository.count() == 0) {
            memberRepository.save(Member.builder().name("윤서준")
                    .email("SeojunYoon@hanbit.co.kr")
                    .age(10)
                    .password(passwordEncoder.encode("password"))
                    .authority("ROLE_USER").build());
            memberRepository.save(Member.builder().name("윤광철")
                    .email("KwangcheolYoon@hanbit.co.kr")
                    .age(43)
                    .password(passwordEncoder.encode("password"))
                    .authority("ROLE_ADMIN").build());
        }
        if (productRepository.count() == 0) {
            productRepository.save(com.example.demo.model.Product.builder()
                    .name("갤럭시 S23")
                    .price(1000000)
                    .description("")
                    .build());
            productRepository.save(com.example.demo.model.Product.builder()
                    .name("아이폰 14")
                    .price(1200000)
                    .description("애플 최신 스마트폰")
                    .build());
        }
    }
}
