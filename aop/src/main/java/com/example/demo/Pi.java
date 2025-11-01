package com.example.demo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
public class Pi {
    @PrintExecutionTime
    double calculate(int points) {
        int circle = 0;
        for (long i = 0; i < points; i++) {
            double x = Math.random() * 2 - 1;
            double y = Math.random() * 2 - 1;
            if (x * x + y * y <= 1) {
                circle++;
            }
        }
        return 4.0 * circle / points;
    }

    @Async
    CompletableFuture<Double> calculateAsync(int points) {
        int circle = 0;
        for (long i = 0; i < points; i++) {
            double x = Math.random() * 2 - 1;
            double y = Math.random() * 2 - 1;
            if (x * x + y * y <= 1) {
                circle++;
            }
        }
        return CompletableFuture.completedFuture(4.0 * circle / points);
    }
}
