package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;

@SpringBootTest
public class ImageMultiModalTests {
    private static final Logger log = LoggerFactory.getLogger(AudioMultiModalTests.class);

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void testMultiModalWithImageInput() {
        Resource resource = new ClassPathResource("/image/Disney_World_1.jpg");
        //Resource resource = new ClassPathResource("/image/Disney_World_2.jpg");
        //Resource resource = new FileSystemResource("D:\\archive\\image\\Disney_World_2.jpg");
        //Resource resource = new UrlResource("https://picsum.photos/200/300");

        Media media = Media.builder()
                .mimeType(MimeTypeUtils.IMAGE_JPEG)
                .data(resource).build();

        Message message = UserMessage.builder()
                //.text("사진에 제목을 붙인다면 무엇이 좋을까?")
                .text("사진속의 풍경을 멋진 시로 써 주세요")
                .media(media).build();

        String result = chatModel.call(message);
        log.info("{}", result);
    }
}
