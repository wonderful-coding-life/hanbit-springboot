package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAiChatModelTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiChatModelTests.class);

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void testChatMemory() {
        Message system = new SystemMessage("간단하고 명료하게 답변해 주세요.");
        Message message1 = new UserMessage("서울 올림픽은 몇회 올림픽이야?");
        Message assistant1 = new AssistantMessage("서울 올림픽은 제24회 하계 올림픽입니다. 1988년 9월 17일부터 10월 2일까지 서울에서 개최되었습니다.");
        Message message2 = new UserMessage("몇개국이나 참가했지?");
        Message assistant2 = new AssistantMessage("1988년 서울 올림픽에는 총 159개국이 참가했습니다. 이 대회는 당시 하계 올림픽 역사상 가장 많은 국가가 참여한 대회 중 하나로 기록되었습니다.");
        Message message3 = new UserMessage("우리나라는 몇개의 메달을 획득했어?");
        String result = chatModel.call(system, message1, assistant1, message2, assistant2, message3);
        log.info("result = {}", result);
    }
}
