package com.example.demo.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatHandler {
    private final OpenAiChatModel chatModel;

    public Mono<ServerResponse> getChatResponse(ServerRequest request) {
        String message = request.queryParam("message").orElse("Tell me a joke");
        Flux<String> completion = chatModel.stream(message);

        log.info("Chat message: {}", message);

        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(completion, String.class);
    }
}
