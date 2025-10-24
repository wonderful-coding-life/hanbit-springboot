package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final OpenAiImageModel imageModel;

    @GetMapping(value = "/api/image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@RequestParam("message") String message) {
        log.info("{}", message);
        var options = OpenAiImageOptions.builder().responseFormat("b64_json").build();
        var response = imageModel.call(new ImagePrompt(message, options));
        var b64Json = response.getResult().getOutput().getB64Json();
        return Base64.getDecoder().decode(b64Json);
    }
}
