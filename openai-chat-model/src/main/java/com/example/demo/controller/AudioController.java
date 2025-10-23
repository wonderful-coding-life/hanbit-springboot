package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class AudioController {
    @Autowired
    private OpenAiChatModel chatModel;

    @GetMapping(value = "/api/audio", produces = "audio/mp3")
    public byte[] getAudio(@RequestParam("message") String message) {
        log.info("/api/audio {}", message);
        ChatOptions chatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                .outputModalities(List.of("text", "audio"))
                .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                        OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.NOVA,
                        OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3)
                ).build();

        Prompt prompt = Prompt.builder()
                .messages(
                        new SystemMessage("간단하고 명료하게 답변해 주세요"),
                        new UserMessage(message)
                ).chatOptions(chatOptions).build();

        ChatResponse response = chatModel.call(prompt);

        log.info("audio transcript {}", response.getResult().getOutput().getText());
        log.info("model {}, prompt {}, completion {}",
                response.getMetadata().getModel(),
                response.getMetadata().getUsage().getPromptTokens(),
                response.getMetadata().getUsage().getCompletionTokens()
        );

        return response.getResult().getOutput().getMedia().getFirst().getDataAsByteArray();
    }
}
