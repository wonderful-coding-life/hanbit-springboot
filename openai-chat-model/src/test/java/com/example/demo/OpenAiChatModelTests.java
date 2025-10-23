package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
public class OpenAiChatModelTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiChatModelTests.class);

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void testChatModelSimple() {
        String result = chatModel.call("서울 올림픽은 몇회 올림픽이야?");
        log.info("result {}", result);
    }

    @Test
    public void testChatModelMessage() {
        var userMessage = new UserMessage("서울 올림픽에 대해 알려 주세요");
        var systemMessage = new SystemMessage("답변은 간략하게 하고, 마지막에는 실제 뉴스를 참고하라는 말을 해 주세요");
        String result = chatModel.call(userMessage, systemMessage);
        log.info("result {}", result);
    }

    @Test
    public void testChatModelMessageContext() {
        Message system = new SystemMessage("간략하게 답변해 주세요.");
        Message message1 = new UserMessage("서울 올림픽에 대해 알려 주세요");
        Message assistant1 = new AssistantMessage("서울 올림픽, 공식명칭은 제24회 하계 올림픽대회는 1988년 9월 17일부터 10월 2일까지 대한민국 서울에서 개최되었습니다. 이 대회는 한국에서 처음으로 열린 올림픽 경기로, 총 159개 국가가 참여하였고 23종목의 경기가 진행되었습니다. 서울 올림픽은 성공적인 개최로 평가받았으며, 그로 인해 한국은 국제 스포츠 무대에서의 위상이 크게 향상되었습니다. 또한, 대회 기간 동안 평화와 화합을 강조하며 많은 이들에게 기억에 남는 현장이 되었습니다.");
        Message message2 = new UserMessage("그럼 바로 그 이전 올림픽은 어디야?");
        Message assistant2 = new AssistantMessage("바로 이전 올림픽은 1984년 하계 올림픽으로, 미국 로스앤젤레스에서 개최되었습니다. 이 대회는 7월 28일부터 8월 12일까지 진행되었고, 많은 국가가 참여한 강행된 올림픽 중 하나였습니다.");
        Message message3 = new UserMessage("그럼 그 두개의 올림픽중 참여 국가는 어디가 많아?");
        String result = chatModel.call(system, message1, assistant1, message2, assistant2, message3);
        log.info("result {}", result);
    }

    @Test
    public void testChatGptPrompt() {
        List<Message> messages = List.of(
                new SystemMessage("간략하게 답변해 주세요."),
                new UserMessage("서울 올림픽에 대해 알려 주세요"),
                new AssistantMessage("서울 올림픽, 공식명칭은 제24회 하계 올림픽대회는 1988년 9월 17일부터 10월 2일까지 대한민국 서울에서 개최되었습니다. 이 대회는 한국에서 처음으로 열린 올림픽 경기로, 총 159개 국가가 참여하였고 23종목의 경기가 진행되었습니다. 서울 올림픽은 성공적인 개최로 평가받았으며, 그로 인해 한국은 국제 스포츠 무대에서의 위상이 크게 향상되었습니다. 또한, 대회 기간 동안 평화와 화합을 강조하며 많은 이들에게 기억에 남는 현장이 되었습니다."),
                new UserMessage("그럼 바로 그 이전 올림픽은 어디야?"),
                new AssistantMessage("바로 이전 올림픽은 1984년 하계 올림픽으로, 미국 로스앤젤레스에서 개최되었습니다. 이 대회는 7월 28일부터 8월 12일까지 진행되었고, 많은 국가가 참여한 강행된 올림픽 중 하나였습니다."),
                new UserMessage("그럼 그 두개의 올림픽중 참여 국가는 어디가 많아?")
        );

        var chatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O)
                .N(2)
                .temperature(1.0).build();

        var prompt = new Prompt(messages, chatOptions);

        var chatResponse = chatModel.call(prompt);

        Usage usage = chatResponse.getMetadata().getUsage();
        log.info("promptTokens {}, completionTokens {}, totalTokens {}",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());

        RateLimit rateLimit = chatResponse.getMetadata().getRateLimit();
        log.info("requestLimit {}, requestRemaining {}, requestReset {}",
                rateLimit.getRequestsLimit(), rateLimit.getRequestsRemaining(), rateLimit.getRequestsReset());
        log.info("tokensLimit {}, tokensRemaining {}, tokensReset {}",
                rateLimit.getTokensLimit(), rateLimit.getTokensRemaining(), rateLimit.getTokensReset());
        for (Generation generation : chatResponse.getResults()) {
            log.info("response {}", generation.getOutput().getText());
        }
    }

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

    @Test
    public void testMultiModalWithAudioInput() {
        // 오디오 미디어 준비
        Resource resource = new ClassPathResource("/audio/sample_audio.mp3");
        Media media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType("audio/mp3"))
                .data(resource).build();

        // 준비한 미디어와 텍스트로 사용자 메시지 준비
        UserMessage message = UserMessage.builder()
                .text("이 오디오 파일의 내용을 요약해 주세요")
                .media(media).build();

        // 기본 GPT 모델은 오디오를 입력으로 받지 못 하므로 오디오 입력이 가능한 챗모델을 지정
        ChatOptions chatOptions = OpenAiChatOptions.builder()
                //.model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                .model("gpt-audio")
                .build();

        // 챗옵션과 사용자 메시지를 사용하여 프롬프트 구성하고 호출
        ChatResponse response = chatModel.call(new Prompt(message, chatOptions));

        // 생성된 답변과 답변을 생성한 모델 이름 출력
        log.info("{}", response.getResult().getOutput().getText());
        log.info("generated by {}", response.getMetadata().getModel());
    }

    @Test
    public void testMultiModalWithAudioOutput() throws IOException {

        String message = "스프링부트에 대해 간단하게 설명해 주세요";

        ChatOptions chatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                //.model("gpt-audio")
                .outputModalities(List.of("text", "audio"))
                .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                        OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.NOVA,
                        OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3)
                ).build();

        ChatResponse response = chatModel.call(new Prompt(message, chatOptions));

        String audioTranscript = response.getResult().getOutput().getText();
        log.info("audio transcript {}", audioTranscript);
        log.info("model {}, prompt {}, completion {}",
                response.getMetadata().getModel(),
                response.getMetadata().getUsage().getPromptTokens(),
                response.getMetadata().getUsage().getCompletionTokens()
        );

        byte[] audio = response.getResult().getOutput().getMedia().getFirst().getDataAsByteArray(); // audio data
        Files.write(Paths.get("D:\\archive\\audio\\ai_chat_audio.mp3"), audio);
    }

    @Test
    public void testMultiModalWithAudioInputOutput() throws IOException {
        var audioResource = new ClassPathResource("/audio/sample_audio_ask.mp3");
        var media = Media.builder()
                .mimeType(MimeTypeUtils.parseMimeType("audio/mp3"))
                .data(audioResource)
                .build();
        var userMessage =UserMessage.builder()
                .text("질문에 친절하고 간략하게 답변해 주세요")
                .media(media)
                .build();

        ChatResponse response = chatModel.call(new Prompt(userMessage,
                OpenAiChatOptions.builder()
                        .temperature(0.5)
                        .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                        .outputModalities(List.of("text", "audio"))
                        .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                                OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.NOVA,
                                OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3)
                        ).build()));

        String text = response.getResult().getOutput().getText(); // audio transcript
        System.out.println("result = " + text);

        byte[] audio = response.getResult().getOutput().getMedia().getFirst().getDataAsByteArray(); // audio data
        Files.write(Paths.get("D:\\archive\\audio\\ai_chat_audio_answer.mp3"), audio);
    }
}
