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
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Order createOrder(List<String> productNumbers, LocalDateTime registeredDateTime) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 재고 체크 및 차감
        for (Product product : products) {
            if (product.getType() == ProductType.BOTTLE || product.getType() == ProductType.BAKERY) {
                Stock stock = stockRepository.findByProductNumber(product.getProductNumber())
                        .orElseThrow(() -> new IllegalArgumentException("재고 정보가 없습니다."));

                long count = productNumbers.stream()
                        .filter(pn -> pn.equals(product.getProductNumber()))
                        .count();

                if (stock.getQuantity() < count) {
                    throw new IllegalArgumentException("재고가 부족합니다.");
                }

                stock.deduct((int) count);
            }
        }

        Order order = new Order(products, registeredDateTime);
        return orderRepository.save(order);
    }
}