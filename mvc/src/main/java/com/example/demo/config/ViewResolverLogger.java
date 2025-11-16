package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewResolverLogger implements ApplicationRunner {
    private final List<ViewResolver> viewResolvers;
    private final ThymeleafViewResolver thymeleafViewResolver;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("view resolver count = {}", viewResolvers.size());
        viewResolvers.forEach(viewResolver -> log.info("{}", viewResolver));
        // pseudocode
        //thymeleafViewResolver.resolveViewName("article-list", Locale.KOREA).render(model, request, response);
    }
}
