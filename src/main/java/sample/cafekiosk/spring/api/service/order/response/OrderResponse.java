package sample.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private LocalDateTime orderDateTime;
    private int totalPrice;
    private List<ProductResponse> products;

    @Builder
    private OrderResponse(Long id, OrderStatus status, LocalDateTime orderDateTime, int totalPrice, List<ProductResponse> products) {
        this.id = id;
        this.status = status;
        this.orderDateTime = orderDateTime;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderDateTime(order.getOrderDateTime())
                .totalPrice(order.getTotalPrice())
                .products(order.getProducts().stream()
                        .map(ProductResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}