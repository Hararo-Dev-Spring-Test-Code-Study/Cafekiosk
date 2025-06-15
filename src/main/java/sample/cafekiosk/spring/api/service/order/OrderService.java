package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    // 상품 번호 리스트를 받아 주문 생성하기
    // 주문은 주문 상태, 주문 등록 시간을 갖는다
    // 총 금액을 계산 해야 한다
    public OrderResponse createOrder(Map<String, Integer> productNumberWithQuantity, LocalDateTime orderDateTime) {
        // 상품 번호 리스트 받아서 상품 찾기 -> Map형으로 바꾸면서 상품번호 리스트 추출
        List<String> productNumbers = productNumberWithQuantity.keySet().stream().toList();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 추가: BOTTLE, BAKERY 상품만 재고를 확인하고 차감
        List<String> stockProducts = products.stream()  // 상품 유형 확인
                .filter(product -> product.getType() == ProductType.BAKERY || product.getType() == ProductType.BOTTLE)
                .map(Product::getProductNumber)
                .collect(Collectors.toList());

        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProducts);   // 재고 조회

        // 재고 차감하기
        for (String productNumber : stockProducts) {
            Stock stock = stocks.stream()
                    .filter(s -> s.getProductNumber().equals(productNumber))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("재고가 부족합니다."));

            Integer orderQuantity = productNumberWithQuantity.get(productNumber);
            stock.deductQuantity(orderQuantity);
        }

        // 주문 생성
        Order order = Order.create(products, orderDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }
}