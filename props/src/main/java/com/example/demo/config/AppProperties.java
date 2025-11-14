package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app") // relaxed binding
public class AppProperties {
    private SchedulerProperties scheduler;

    @Data
    public static class SchedulerProperties {
        private TaskProperties orderFulfillment;
        private TaskProperties cancelUnpaidOrders;
        private TaskProperties requestPostPurchaseReview;
    }

    @Data
    public static class TaskProperties {
        private boolean enabled;
        private String cron;
    }
}
