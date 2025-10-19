package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootTest
public class OpenAiAudioTranscriptionModelTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiAudioTranscriptionModelTests.class);

    @Autowired
    private OpenAiAudioTranscriptionModel transcriptionModel;

    @Test
    public void testTranscriptModel() {
        Resource resource = new ClassPathResource("/sample_audio.mp3");
        String result = transcriptionModel.call(resource);
        log.info("transcript {}", result);
    }

    @Test
    public void testTranscriptModelOptions() {
        OpenAiAudioTranscriptionOptions openAiAudioTranscriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .model("whisper-1") // whisper-1 (default), gpt-4o-transcribe, gpt-4o-mini-transcribe
                .language("ko") // ko, en, ja 등 90개 이상의 언어 (설정하지 않으면 auto detect)
                // 현재 GranularityType은 동작하지 않음
                //.granularityType(OpenAiAudioApi.TranscriptionRequest.GranularityType.SEGMENT)
                //.responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VERBOSE_JSON)
                .build();

        Resource resource = new ClassPathResource("/audio/sample_audio.mp3");
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(resource, openAiAudioTranscriptionOptions);
        AudioTranscriptionResponse response = transcriptionModel.call(prompt);

        log.info("transcript {}", response.getResult().getOutput());
    }
}
