package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="app.pi.enabled", havingValue = "true")
public class PiAsyncApplication implements ApplicationRunner {
    @Autowired
    private Pi pi;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var future = pi.calculateAsync(100000000);
        future.thenAccept(pi -> System.out.println("Async PI = " + pi));
        System.out.println("Continue my jobs after kicking off pi calculation");
        for (int i = 0; i < 5; i++) {
            System.out.println("working..." + i);
            Thread.sleep(500);
        }
        System.out.println("Application Runner has finished");
        // 이 Application Runner는 종료하지만 @Async 스레드풀을 스프링이 “안전하게” 종료하려고 기다리기 때문에 약 1분 정도 후에 애플리케이션 종료 --> Graceful Shutdown
    }
}
