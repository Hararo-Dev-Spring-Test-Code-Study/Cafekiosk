package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.stock.StockRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.stock.Stock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public Order createOrder(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 재고 확인
        List<String> stockProductNumbers = products.stream()
                .filter(p -> p.getType().requiresStock())  // ProductType에 메서드 추가 필요
                .map(Product::getProductNumber)
                .toList();

        Map<String, Long> productCountMap = productNumbers.stream()
                .collect(Collectors.groupingBy(pn -> pn, Collectors.counting()));

        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

        for (String productNumber : stockProductNumbers) {
            long orderedCount = productCountMap.getOrDefault(productNumber, 0L);
            Stock stock = stocks.stream()
                    .filter(s -> s.getProductNumber().equals(productNumber))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("재고 정보 없음"));

            if (stock.isQuantityLessThan(orderedCount)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 포함되어 있습니다.");
            }

            stock.deductQuantity(orderedCount); // 재고 차감
        }

        Order order = Order.create(products, LocalDateTime.now());
        return orderRepository.save(order);
    }

}

