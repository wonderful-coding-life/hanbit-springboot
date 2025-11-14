package com.example.demo;

import com.example.demo.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MulticampusShopApplication implements ApplicationRunner {
    private final AppProperties appProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("app properties = {}", appProperties);
    }
}
