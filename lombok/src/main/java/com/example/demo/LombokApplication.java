package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LombokApplication implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Product p1 = Product.builder().name("바나나").description("맛있는 상품").price(1000).build();
        Product p2 = new Product("바나나", "노란 상품", 1000);
        Product p3 = new Product();
        p3.setName("바나나");
        p3.setDescription("신선한 상품");
        p3.setPrice(1000);

        log.info("p1 = {}, p2 = {}, p3 = {}", p1, p2, p3);
        log.info("p1.equals(p2) = " + p1.equals(p2));
        log.info("p2.equals(p3) = " + p2.equals(p3));
    }
}
