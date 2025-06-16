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
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public Order createOrder(List<String> productNumbers) {
        // 1. 상품 조회 (중복된 상품번호 고려해서 수량 맞추기)
        List<Product> allProducts = getOrderedProductsWithQuantity(productNumbers);

        // 2. 재고 확인이 필요한 상품 번호 추출
        Set<String> stockProductNumbers = new HashSet<>();
        for (Product product : allProducts) {
            if (product.getType().requiresStock()) {
                stockProductNumbers.add(product.getProductNumber());
            }
        }

        // 3. 상품별 주문 수량 계산
        Map<String, Long> productCountMap = new HashMap<>();
        for (String number : productNumbers) {
            productCountMap.put(number, productCountMap.getOrDefault(number, 0L) + 1);
        }

        // 4. 재고 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(new ArrayList<>(stockProductNumbers));

        // 5. 재고 확인 및 차감
        for (String productNumber : stockProductNumbers) {
            long orderedCount = productCountMap.get(productNumber);

            Stock matchedStock = null;
            for (Stock stock : stocks) {
                if (stock.getProductNumber().equals(productNumber)) {
                    matchedStock = stock;
                    break;
                }
            }

            if (matchedStock == null) {
                throw new IllegalArgumentException("재고 정보 없음");
            }

            if (matchedStock.isQuantityLessThan(orderedCount)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 포함되어 있습니다.");
            }

            matchedStock.deductQuantity(orderedCount);
        }

        // 6. 주문 생성 및 저장
        Order order = Order.create(allProducts, LocalDateTime.now());
        return orderRepository.save(order);
    }

    private List<Product> getOrderedProductsWithQuantity(List<String> productNumbers) {
        List<Product> productsFromDb = productRepository.findAllByProductNumberIn(productNumbers);

        // productNumber → Product 매핑
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : productsFromDb) {
            productMap.put(product.getProductNumber(), product);
        }

        // 주문 수량에 맞춰 Product 복제
        List<Product> orderedProducts = new ArrayList<>();
        for (String number : productNumbers) {
            Product product = productMap.get(number);
            if (product != null) {
                orderedProducts.add(product);
            }
        }
        return orderedProducts;
    }
}

