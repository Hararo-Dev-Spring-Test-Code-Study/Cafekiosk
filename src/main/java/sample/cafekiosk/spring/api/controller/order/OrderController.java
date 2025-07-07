package sample.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        // RequestBody를 붙여줘야 post에서 body로 넘어오는애를 받음
        LocalDateTime registeredDateTime = LocalDateTime.now();

        return orderService.createOrder(request, registeredDateTime);
    }
}
