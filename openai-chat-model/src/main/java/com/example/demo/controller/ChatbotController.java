package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ChatbotController {

    @Autowired
    private OpenAiChatModel chatModel;

    @Autowired
    private ChatMemory chatMemory;

    @GetMapping("/api/chatbot")
    public String getChat(@RequestParam("id") String id, @RequestParam("message") String message) {

        // 채팅 시작시 시스템 메시지 추가되며 윈도우 사이즈가 넘어가더라도 유지
        // 문서에 따르면 SystemMessage가 추가되면 기존 SystemMessage들은 모두 삭제
        if (chatMemory.get(id).isEmpty()) {
            chatMemory.add(id, new SystemMessage("정확하고 명료하게 답변 해 주세요"));
        }

        // 사용자가 전달한 유저 메시지 추가
        UserMessage userMessage = new UserMessage(message);
        chatMemory.add(id, userMessage);

        // 챗메모리에 있는 메시지로 프롬프트 구성하고 대화형 AI 모델 호출
        Prompt prompt = new Prompt(chatMemory.get(id));
        ChatResponse chatResponse = chatModel.call(prompt);

        // AI 모델 응답을 챗메모리에 추가
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        chatMemory.add(id, assistantMessage);

        // 필요시 로그로 확인
        log.info("chatMemory size = {}", chatMemory.get(id).size());
        List<Message> messages = chatMemory.get(id);
        messages.forEach(msg -> log.info("{}", msg.getText()));

        return assistantMessage.getText();
    }
}
