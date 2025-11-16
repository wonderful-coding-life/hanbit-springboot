package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandlerMappingLogger implements ApplicationRunner {
    private final List<RequestMappingHandlerMapping> mappings;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mappings.forEach(mapping ->
            mapping.getHandlerMethods().forEach((info, method) ->
                log.info("info {} -> {}", info, method)
            )
        );
    }
}
