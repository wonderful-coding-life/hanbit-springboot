package com.example.demo;

import org.junit.jupiter.api.Test;
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

    @Autowired
    private OpenAiAudioSpeechModel speechModel;

    @Test
    public void testSeechModelSimple() throws IOException {
        byte[] bin = speechModel.call("안녕하세요 반갑습니다. 스프링부트는 자바 프레임워크 중에 가장 인기가 많은 프레임워크입니다.");
        Files.write(Paths.get("D:\\archive\\audio\\ai_tts_simple.mp3"), bin);
    }

    @Test
    public void testSpeechModelOptions() throws IOException {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .model("tts-1-hd") // tts-1, tts-1-hd
                .voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA) // default ALLOY?
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build();

        String textToSpeech = "안녕하세요 반갑습니다. 스프링부트는 자바 프레임워크 중에 가장 인기가 많은 프레임워크입니다.";

        SpeechPrompt speechPrompt = new SpeechPrompt(textToSpeech, speechOptions);
        SpeechResponse response = speechModel.call(speechPrompt);
        System.out.println("meta = " + response.getMetadata());

        RateLimit rateLimit = response.getMetadata().getRateLimit();
        if (rateLimit != null) {
            System.out.println("requestLimit = " + rateLimit.getRequestsLimit() +
                    ", requestRemaining = " + rateLimit.getRequestsRemaining() +
                    ", requestReset = " + rateLimit.getRequestsReset());
            System.out.println("tokensLimit = " + rateLimit.getTokensLimit() +
                    ", tokensRemaining = " + rateLimit.getTokensRemaining() +
                    ", tokensReset = " + rateLimit.getTokensReset());
        }

        Files.write(Paths.get("D:\\archive\\audio\\ai_tts_options.mp3"), response.getResult().getOutput());
    }
}
