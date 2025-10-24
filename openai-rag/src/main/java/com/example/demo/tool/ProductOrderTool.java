package com.example.demo.tool;

import com.example.demo.model.ProductOrder;
import com.example.demo.repository.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductOrderTool {
    private final ProductOrderRepository productOrderRepository;

    @Tool(description = "상품 주문 목록을 알려준다")
    String getProductOrders() {
        String result = "주문 목록은 다음과 같습니다.\n";
        var productOrders = productOrderRepository.findAll();
        for (ProductOrder productOrder : productOrders) {
            result += "주문번호: " + productOrder.getOrderNumber()
                    + ", 상품이름: " + productOrder.getProductName()
                    + ", 배송주소: " + productOrder.getShippingAddress()
                    + ", 배송상태: " + productOrder.getShippingStatus()
                    + "\n";
        }
        return result;
    }

    @Tool(description = "상품 주문을 취소한다")
    String cancelProductOrder(@ToolParam(description = "주문번호") String orderNumber) {
        var productOrder = productOrderRepository.findByOrderNumber(orderNumber);
        if (productOrder.isPresent()) {
            if ("배송중".equals(productOrder.get().getShippingStatus())) {
                return "배송중인 상품은 취소할 수 없습니다";
            } else {
                productOrderRepository.delete(productOrder.get());
                return "주문이 취소되었습니다.";
            }
        } else {
            return "없는 주문 번호입니다.";
        }
    }
}
