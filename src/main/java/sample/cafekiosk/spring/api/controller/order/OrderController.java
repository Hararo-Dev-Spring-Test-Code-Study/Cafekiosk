package sample.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.cafekiosk.spring.api.APIResponse;
import sample.cafekiosk.spring.api.dto.order.OrderRequest;
import sample.cafekiosk.spring.api.dto.order.OrderResponse;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.domain.order.Order;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/new")
    public ResponseEntity<APIResponse<String>> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request.getProductNumbers());
        return ResponseEntity.ok(
                APIResponse.success(order.getId().toString())
        );
    }
}