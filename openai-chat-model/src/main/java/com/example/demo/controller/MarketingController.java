package com.example.demo.controller;

import com.example.demo.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.MessageFormat;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MarketingController {
    private final OpenAiChatModel chatModel;

    @GetMapping("/marketing")
    public String getMarketing() {
        return "marketing-request";
    }

    @PostMapping("/marketing")
    public String postMarketing(Product product, Model model) {
        var systemMessage = new SystemMessage("""
                너는 전문 마케팅 카피라이터야.
                입력된 제품 정보를 기반으로 온라인 쇼핑몰/블로그/홍보 페이지에 사용할 매력적인 마케팅 문구를 작성해 줘.
                작성 조건
                1. 소비자의 관심을 끌 수 있도록 첫 문장은 강렬하거나 공감 가는 표현을 사용해.
                2. 제품 특징을 자연스럽게 녹여서 장점이 잘 드러나게 작성해.
                3. 가격과 구매 링크는 구매를 자극하는 문구와 함께 포함시켜.
                   - 예: "지금 {가격}에 만나보세요 👉 {구매 링크}"
                4. 글자 수는 약 300~500자로 하고, 캐주얼하지만 설득력 있는 톤으로 작성해.
                5. 필요하면 감각적인 이모지도 활용해.
            """);

        var userMessage = new UserMessage(MessageFormat.format("""
                ### 입력 정보
                - 제품명: {0}
                - 가격: {1}
                - 구매 링크: {2}
                - 제품 특징: {3}
            """ , product.getName(),
                product.getPrice(),
                product.getLink(),
                product.getFeatures()));

        String result = chatModel.call(userMessage, systemMessage);
        log.info("result = {}", result);
        model.addAttribute("marketingResult", result);

        return "marketing-response";
    }
}
