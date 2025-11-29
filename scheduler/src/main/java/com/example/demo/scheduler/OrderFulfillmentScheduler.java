package com.example.demo.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
    prefix = "app.scheduler.order-fulfillment",
    name = "enabled",
    havingValue = "true"
)
public class OrderFulfillmentScheduler {
    @Scheduled(cron = "${app.scheduler.order-fulfillment.cron}")
    public void dispatchOrdersToFulfillment() {
        log.info("Dispatching orders to fulfillment...");
        // TODO: 주문 조회 → 풀필먼트 시스템으로 전송 로직
    }
}
