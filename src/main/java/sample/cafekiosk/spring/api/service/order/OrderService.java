package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 적용
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Transactional // 쓰기 작업이므로 readOnly 해제 (기본값 false)
    public Order createOrder(List<String> productNumbers, LocalDateTime registeredDateTime) {
        // 사용자가 주문한 상품번호 목록으로 상품 엔티티들을 조회
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 재고를 확인하고 차감하는 로직 수행
        for (Product product : products) {
            // 병음료나 베이커리인 경우 재고를 체크
            if (product.getType() == ProductType.BOTTLE || product.getType() == ProductType.BAKERY) {
                // 상품 번호로 재고 정보를 조회 (없을 경우 예외 발생)
                Stock stock = stockRepository.findByProductNumber(product.getProductNumber())
                        .orElseThrow(() -> new IllegalArgumentException("재고 정보가 없습니다."));

                // 같은 상품 번호가 주문 목록에 몇 번 포함되어 있는지 계산
                long count = productNumbers.stream()
                        .filter(pn -> pn.equals(product.getProductNumber()))
                        .count();

                // 재고보다 많은 수량을 주문한 경우 예외 발생
                if (stock.getQuantity() < count) {
                    throw new IllegalArgumentException("재고가 부족합니다.");
                }

                // 재고 차감
                stock.deduct((int) count);
            }
        }

        // 주문을 생성하고 저장 (중복된 상품도 가격에 반영됨)
        Order order = new Order(
                productNumbers.stream()
                        .map(pn -> products.stream()
                                .filter(p -> p.getProductNumber().equals(pn))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + pn)))
                        .toList(),
                registeredDateTime
        );

        return orderRepository.save(order);
    }
}