package com.example.demo.tool;

import com.example.demo.repository.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductOrderTool {
    private final ProductOrderRepository productOrderRepository;

    @Tool(description = "상품 주문 목록을 알려줍니다")
    String getProductOrders() {
        String result = "주문 목록은 다음과 같아요\n";
        var productOrders = productOrderRepository.findAll();
        for (var productOrder : productOrders) {
            result += "주문번호: " + productOrder.getOrderNumber();
            result += ", 상품이름: " + productOrder.getProductName();
            result += ", 배송주소: " + productOrder.getShippingAddress();
            result += ", 배송상태: " + productOrder.getShippingStatus();
            result += "\n";
        }
        return result;
    }

    @Tool(description = "상품을 취소한다")
    String cancelProductOrder(@ToolParam(description = "주문번호") String orderNumber) {
        var productOrder = productOrderRepository.findByOrderNumber(orderNumber);
        if (productOrder.isPresent()) {
            if ("배송중".equals(productOrder.get().getShippingStatus())) {
                return "배송중인 상품은 취소할 수 없습니다.";
            } else {
                productOrderRepository.delete(productOrder.get());
                return "주문이 취소 되었습니다.";
            }
        } else {
            return "없는 주문 번호입니다.";
        }
    }
}
