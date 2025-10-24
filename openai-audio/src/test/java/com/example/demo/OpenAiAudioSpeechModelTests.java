package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
public class OpenAiAudioSpeechModelTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiAudioSpeechModelTests.class);

    @Autowired
    private OpenAiAudioSpeechModel speechModel;

    @Test
    public void testSpeechModelSimple() throws IOException {
        byte[] bin = speechModel.call("안녕하세요 반갑습니다. 스프링부트는 자바 프레임워크 중에 가장 인기가 많은 프레임워크입니다.");
        Files.write(Paths.get("D:\\archive\\audio\\ai_tts_simple.mp3"), bin);
    }

    @Test
    public void testSpeechModelOptions() throws IOException {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .model("tts-1") // tts-1, tts-1-hd, gpt-4o-mini-tts (not ready yet for spring ai)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA) // default ALLOY?
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build();

        String text = "안녕하세요 반갑습니다. 스프링부트는 자바 프레임워크 중에 가장 인기가 많은 프레임워크입니다.";

        SpeechPrompt speechPrompt = new SpeechPrompt(text, speechOptions);
        SpeechResponse response = speechModel.call(speechPrompt);
        log.info("Metadata {}", response.getMetadata());

        RateLimit rateLimit = response.getMetadata().getRateLimit();
        if (rateLimit != null) {
            log.info("requestLimit = {}, requestRemaining = {}, requestReset = {}",
                    rateLimit.getRequestsLimit(),
                    rateLimit.getRequestsRemaining(),
                    rateLimit.getRequestsReset());
            // 텍스트 토큰을 사용하지 않고 오디오 길이(초) 기준으로 과금되기 때문에 토큰 제한은 null이 반환
            log.info("tokensLimit = {}, tokensRemaining = {}, tokensReset = {}",
                    rateLimit.getTokensLimit(),
                    rateLimit.getTokensRemaining(),
                    rateLimit.getTokensReset());
        }

        byte[] bin = response.getResult().getOutput();
        Files.write(Paths.get("D:\\archive\\audio\\ai_tts_options.mp3"), bin);
    }
}
