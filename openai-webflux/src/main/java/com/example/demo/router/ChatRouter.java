package com.example.demo.router;

import com.example.demo.handler.ChatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ChatRouter {
    @Bean
    public RouterFunction<ServerResponse> route(ChatHandler chatHandler) {
        return RouterFunctions.route()
                .GET("/router/chat", chatHandler::getChatResponse)
                .build();
    }
}
