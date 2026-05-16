package com.example.demo;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DripCoffeeMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Drip Coffee Machine";
    }
}
