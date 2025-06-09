package sample.cafekiosk.spring.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private OrderStatus orderStatus;
    private LocalDateTime registeredDateTime;
    private int totalPrice;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getOrderStatus(),
                order.getRegisteredDateTime(),
                order.getTotalPrice()
        );
    }
}
