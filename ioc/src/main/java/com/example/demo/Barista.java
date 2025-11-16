package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Barista {
    @Autowired
    private List<CoffeeMachine> coffeeMachines;

    public void makeCoffees() {
        for (CoffeeMachine coffeeMachine : coffeeMachines) {
            System.out.println(coffeeMachine.brew());
        }
    }
}
