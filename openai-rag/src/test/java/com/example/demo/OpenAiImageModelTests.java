package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAiImageModelTests {

    @Autowired
    private OpenAiImageModel imageModel;

    @Test
    public void testImageModelOptions() {
        String message = """
            수채화 스타일로 그린 화성 탐사 로버 그림이 필요해.
            2족 보행 로봇이 함께 탐사하는 모습으로 해 줘.
            붓으로 그린 듯한 부드러운 필치와 여백의 미를 살려서 표현해 줘.
            """;
        ImageResponse response = imageModel.call(
                new ImagePrompt(message, OpenAiImageOptions.builder()
                    .model("dall-e-3") // model = 'dall-e-2', 'dall-e-3'
                    .quality("hd") // quality = 'hd', 'standard'
                    .style("natural") // style = 'vivid', 'natural'
                    .width(1024)
                    .height(1024).build()));

        System.out.println("result = " + response.getResult().getOutput().getUrl());
    }
}
