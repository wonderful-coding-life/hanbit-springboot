package com.example.demo.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
    prefix = "app.scheduler.cancel-unpaid-orders",
    name = "enabled",
    havingValue = "true"
)
public class CancelUnpaidOrdersScheduler {
    @Scheduled(cron = "${app.scheduler.cancel-unpaid-orders.cron}")
    public void cancelUnpaidOrders() {
        log.info("Canceling unpaid orders...");
        // TODO: 미입금 주문 조회 → 상태 CANCEL로 변경
    }
}
