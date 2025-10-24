package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionOptions;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootTest
public class OpenAiTranscriptionModelTests {

    @Autowired
    private OpenAiAudioTranscriptionModel transcriptionModel;

    @Test
    public void testTranscriptModel() {
        Resource resource = new ClassPathResource("/sample_audio.mp3");
        String result = transcriptionModel.call(resource);
        System.out.println("result = " + result);
    }

    @Test
    public void testTranscriptModelOptions() {
        AudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .language("ja")
                .build();

        Resource resource = new ClassPathResource("/sample_audio.mp3");
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(resource, options);
        AudioTranscriptionResponse response = transcriptionModel.call(prompt);

        System.out.println("result = " + response.getResult().getOutput());
    }
}
