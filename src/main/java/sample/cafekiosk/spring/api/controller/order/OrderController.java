package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.response.APIResponse;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;

@RequiredArgsConstructor
@RestController
public class OrderController {

  private final OrderService orderService;

  @PostMapping("api/v1/orders")
  public ResponseEntity<APIResponse<OrderResponse>> createOrder(
      @RequestBody @Valid OrderCreateRequest request) {
    OrderResponse order = orderService.createOrder(request, LocalDateTime.now());
    return ResponseEntity.ok(
        APIResponse.<OrderResponse>builder()
            .code(200)
            .status("OK")
            .message("주문이 성공적으로 생성되었습니다.")
            .data(order)
            .build()
    );
  }
}
