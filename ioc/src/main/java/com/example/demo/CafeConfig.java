package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 단순한 Bean 객체는 보통 컴포넌트 스캔(@Component, @Service 등)을 사용해 등록한다.
// 하지만 Bean 생성 과정에서 초기화나 설정과 같은 추가 로직이 필요한 경우에는
// @Configuration 클래스의 메서드에 @Bean을 사용한다.
// 이 경우 메서드가 반환하는 객체가 스프링 Bean으로 등록된다.
@Configuration
public class CafeConfig {
    @Bean
    public CoffeeMachine coffeeMachine() {
        String coffeeMachine = System.getenv("COFFEE_MACHINE");
        if ("ESPRESSO".equalsIgnoreCase(coffeeMachine)) {
            return new EspressoMachine();
        }
        return new DripCoffeeMachine();
    }
}
