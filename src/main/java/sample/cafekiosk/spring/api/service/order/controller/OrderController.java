package sample.cafekiosk.spring.api.service.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sample.cafekiosk.spring.api.common.APIResponse;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public APIResponse<Order> createOrder(@RequestBody CreateOrderRequest request) {
        LocalDateTime now = LocalDateTime.now(); // 현재 시각 생성
        Order order = orderService.createOrder(request.getProductNumbers(), now);
        return APIResponse.success(order);
    }
}
