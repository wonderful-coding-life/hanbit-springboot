package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogApplication implements ApplicationRunner {
    //private static final Logger log = LoggerFactory.getLogger(LogApplication.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.trace("application started");
        log.debug("application started");
        log.info("application started");
        log.warn("application started");
        log.error("application started");
    }

//    @Scheduled(fixedDelay = 1000)
//    @Scheduled(fixedRate = 1000)
//    @Scheduled(initialDelay = 1000, fixedDelay = 3000)
    @Scheduled(cron = "0/5 * * * * *") // 초(0~59), 분(0~59), 시(0~23), 일(1~31), 월(1~12), 요일(0~6 일월화수목금토일), 단위(예 0/10 매10초, 0/5 매5분)
    public void scheduledWithFixedDelay() {
        log.trace("scheduled task");
        log.debug("scheduled task");
        log.info("scheduled task");
        log.warn("scheduled task");
        log.error("scheduled task");
    }
}
