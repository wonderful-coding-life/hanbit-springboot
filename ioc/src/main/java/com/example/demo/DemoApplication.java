package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		var context = SpringApplication.run(DemoApplication.class, args);
        System.out.println("beans count = " + context.getBeanDefinitionCount());
        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println("bean " + beanName);
        }
	}
}
