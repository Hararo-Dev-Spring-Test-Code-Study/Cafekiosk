package sample.cafekiosk.spring.api.service.order.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {
    private List<String> productNumbers;
}
