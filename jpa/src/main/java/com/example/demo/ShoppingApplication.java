package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShoppingApplication implements ApplicationRunner {
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        var member = memberRepository.findById(1L).orElseThrow();
        var product = productRepository.findById(1L).orElseThrow();

        var order = ProductOrder.builder().member(member).product(product).build();
        productOrderRepository.save(order);
        log.info("{}", order);

        var orders = productOrderRepository.findAll();
        log.info("{}", orders);

        orders = productOrderRepository.findByMember(member);
        log.info("{}", orders);

        orders = productOrderRepository.findByMember(member);
        log.info("{}", orders);
    }
}
