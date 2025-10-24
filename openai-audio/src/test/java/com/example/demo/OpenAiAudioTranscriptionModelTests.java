package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.metadata.audio.OpenAiAudioTranscriptionResponseMetadata;
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
    public void testTranscriptModelSimple() {
        Resource resource = new ClassPathResource("/audio/sample_audio.mp3");
        //Resource resource = new ClassPathResource("/image/Disney_World_2.jpg");
        //Resource resource = new FileSystemResource("D:\\archive\\audio\\sample_audio.mpg");
        //Resource resource = new UrlResource("https://xxx/sample_audio.mpg");
        String script = transcriptionModel.call(resource);
        log.info("script {}", script);
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

        if (response.getResult().getMetadata() instanceof OpenAiAudioTranscriptionResponseMetadata metadata) {
            RateLimit rateLimit = metadata.getRateLimit();
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
        }
        log.info("transcript {}", response.getResult().getOutput());
    }
}
