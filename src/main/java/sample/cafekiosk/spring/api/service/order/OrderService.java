package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 상품 번호 리스트를 받아 주문 생성하기
    // 주문은 주문 상태, 주문 등록 시간을 갖는다
    // 총 금액을 계산 해야 한다
    public OrderResponse createOrder(List<String> productNumbers, LocalDateTime orderDateTime) {
        // 상품 번호 리스트 받아서 상품 찾기
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 주문 생성
        Order order = Order.create(products, orderDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }
}