package sample.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderStatisticsRequest;
import sample.cafekiosk.spring.api.service.order.OrderStatisticsService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class OrderStatisticsController {

    private final OrderStatisticsService orderStatisticsService;

    @PostMapping("/api/v1/orders/statistics")
    public ApiResponse<Boolean> sendOrderStatistics(@Valid @RequestBody OrderStatisticsRequest request) {
        boolean result = orderStatisticsService.sendOrderStatisticsMail(request.getOrderDate(), request.getEmail());
        return ApiResponse.ok(result);
    }
}