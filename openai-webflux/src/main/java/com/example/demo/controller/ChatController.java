package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final OpenAiChatModel chatModel;

    @GetMapping("/chat")
    public Flux<String> getChatResponse(@RequestParam("message") String message) {
        log.info("Chat message: {}", message);
        return chatModel.stream(message);
    }
}
