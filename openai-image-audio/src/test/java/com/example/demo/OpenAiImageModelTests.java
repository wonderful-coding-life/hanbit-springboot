package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageGenerationMetadata;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.metadata.OpenAiImageGenerationMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@SpringBootTest
public class OpenAiImageModelTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiImageModelTests.class);

    @Autowired
    private OpenAiImageModel imageModel;

    @Test
    public void testImageModelSimple() {
        String message = """
            수채화 스타일로 그린 화성 탐사 로버 그림이 필요해.
            2족 보행 로봇이 함께 탐사하는 모습으로 해 줘.
            붓으로 그린 듯한 부드러운 필치와 여백의 미를 살려서 표현해 줘.
            """;

        ImageResponse response = imageModel.call(new ImagePrompt(message));
        log.info("URL {}", response.getResult().getOutput().getUrl());
    }

    @Test
    public void testImageModel() throws IOException {
        String message = """
            수채화 스타일로 그린 화성 탐사 로버 그림이 필요해.
            2족 보행 로봇이 함께 탐사하는 모습으로 해 줘.
            붓으로 그린 듯한 부드러운 필치와 여백의 미를 살려서 표현해 줘.
            """;

        OpenAiImageOptions openAiImageOptions = OpenAiImageOptions.builder()
                .model("dall-e-3") // dall-e-3, gpt-image-1-mini (protected)
                .style("vivid") // vivid (default), natural
                .quality("hd") // standard (default), hd
                .responseFormat("b64_json") // url (default), b64_json
                .width(1024)
                .height(1024).build();

        ImagePrompt imagePrompt = new ImagePrompt(message, openAiImageOptions);
        ImageResponse imageResponse = imageModel.call(imagePrompt);

        if (imageResponse.getResult().getOutput().getB64Json() != null) {
            // OpenAI와 통신은 JSON으로 이루어지고, JSON 내에서 바이너리를 표현하기 위해 Base64 인코딩을 함
            log.info("Base64Json {}", imageResponse.getResult().getOutput().getB64Json());
            byte[] imageBytes = Base64.getDecoder().decode(imageResponse.getResult().getOutput().getB64Json());
            // dall-e-3는 png 포맷만 지원, 다른 포맷이 필요하다면 애플리케이션에서 변환해야 함
            Files.write(Paths.get("D:\\archive\\image\\openai-image.png"), imageBytes);
        }

        if (imageResponse.getResult().getOutput().getUrl() != null) {
            log.info("Url {}", imageResponse.getResult().getOutput().getUrl());
        }

        ImageGenerationMetadata metadata = imageResponse.getResult().getMetadata();
        // Java 16+ 에서 적용된 패턴 매칭 문법으로 명시적으로 형변환(cast)을 하지 않더라도 open이라는 새로운 지역 변수를 만들어 줌
        if (metadata instanceof OpenAiImageGenerationMetadata open) {
            // OpenAI 모델이 실제 이미지 생성을 위해 내부적으로 재작성한 프롬프트 텍스트
            // 즉, 사용자가 입력한 프롬프트를 모델이 더 명확하고 구체적으로 이해하기 위해 수정
            String revised = open.getRevisedPrompt();
            log.info("revisedPrompt {} ", revised);
        }
    }
}
