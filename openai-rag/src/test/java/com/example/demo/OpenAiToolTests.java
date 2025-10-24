package com.example.demo;

import com.example.demo.model.ProductOrder;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.tool.ProductOrderTool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenAiToolTests {
    private static final Logger log = LoggerFactory.getLogger(OpenAiToolTests.class);
    @Autowired
    private OpenAiChatModel chatModel;
    @Autowired
    private ProductOrderTool productOrderTool;
    @Autowired
    private ProductOrderRepository productOrderRepository;

    @BeforeEach
    public void beforeEach() {
        productOrderRepository.save(ProductOrder.builder()
                        .orderNumber("1000000")
                        .productName("맥북에어")
                        .shippingAddress("서울시 영등포구 여의도동")
                        .shippingStatus("배송중").build());
        productOrderRepository.save(ProductOrder.builder()
                .orderNumber("1000001")
                .productName("아이폰")
                .shippingAddress("서울시 강남구 역삼동")
                .shippingStatus("준비중").build());
    }

    @AfterEach
    public void afterEach() {
        productOrderRepository.deleteAll();
    }

    @Test
    public void testProductOrder() {

        ToolCallback[] productOrderTools = ToolCallbacks.from(this.productOrderTool);
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(productOrderTools)
                .build();

        // 제가 주문한 상품들의 목록을 알려 주세요.
        // 맥북에어 배송 상태를 알려 주세요.
        // 맥북에어 주문을 취소해 주세요.
        // 삼성 노트북 주문을 취소해 주세요.
        Prompt prompt = new Prompt("맥북에어 주문을 취소해 주세요.", chatOptions);
        ChatResponse chatResponse = chatModel.call(prompt);

        Usage usage = chatResponse.getMetadata().getUsage();
        System.out.println("promptTokens = " + usage.getPromptTokens() + ", completionTokens = " + usage.getCompletionTokens() + ", totalTokens = " + usage.getTotalTokens());

        RateLimit rateLimit = chatResponse.getMetadata().getRateLimit();
        System.out.println("requestLimit = " + rateLimit.getRequestsLimit() +
                ", requestRemaining = " + rateLimit.getRequestsRemaining() +
                ", requestReset = " + rateLimit.getRequestsReset());
        System.out.println("tokensLimit = " + rateLimit.getTokensLimit() +
                ", tokensRemaining = " + rateLimit.getTokensRemaining() +
                ", tokensReset = " + rateLimit.getTokensReset());

        for (Generation generation : chatResponse.getResults()) {
            System.out.println("response = " + generation.getOutput().getText());
        }
    }
}
