package com.example.demo.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
    prefix = "app.scheduler.request-post-purchase-review",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class PostPurchaseReviewScheduler {
    @Scheduled(cron = "${app.scheduler.request-post-purchase-review.cron}")
    public void requestPostPurchaseReview() {
        log.info("Sending post-purchase review requests...");
        // TODO: 배송완료 후 N일 지난 주문 → 고객에게 리뷰 요청 발송
    }
}
