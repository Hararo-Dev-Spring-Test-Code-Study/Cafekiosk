package sample.cafekiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OrderCreateRequest {

    // 상품번호(String) 에 대한 주문 갯수(Integer) Map
    private Map<String, Integer> productQuantities;

    @Builder
    public OrderCreateRequest(Map<String, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }
}
