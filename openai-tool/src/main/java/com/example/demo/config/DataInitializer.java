package com.example.demo.config;

import com.example.demo.model.ProductOrder;
import com.example.demo.repository.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final ProductOrderRepository productOrderRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        productOrderRepository.save(ProductOrder.builder()
                .orderNumber("1000000")
                .productName("맥북에어")
                .shippingAddress("서울시 영등포구 여의도동")
                .shippingStatus("배송중").build());
        productOrderRepository.save(ProductOrder.builder()
                .orderNumber("1000001")
                .productName("아이폰")
                .shippingAddress("서울시 강남구 역삼동")
                .shippingStatus("준비중").build());
    }
}
