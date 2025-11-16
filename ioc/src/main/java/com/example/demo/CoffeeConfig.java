package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeConfig {
    @Bean
    public CoffeeMachine espressoMachineBean() {
        return new EspressoMachine();
    }

    @Bean
    public CoffeeMachine dripCoffeeMachineBean() {
        return new DripCoffeeMachine();
    }

    @Bean
    public CoffeeMachine mochaCoffeeMachineBean() {
//        return new CoffeeMachine() {
//            @Override
//            public String brew() {
//                return "brewing coffee with Mocha Coffee Machine";
//            }
//        };

        return () -> "brewing coffee with Mocha Coffee Machine";
    }
}
