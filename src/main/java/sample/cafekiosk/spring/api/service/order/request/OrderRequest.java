package sample.cafekiosk.spring.api.service.order.request;

import lombok.Getter;

@Getter
public class OrderRequest {
    private String productNumber;
    private int quantity;
}