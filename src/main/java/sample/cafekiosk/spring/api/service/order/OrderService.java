package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    // 재고 관련 있는 타입들 : 필요시 추가 가능하도록
    private static final Set<ProductType> STOCK_PRODUCT_TYPE = Set.of(ProductType.BOTTLE, ProductType.BAKERY);

    // 더티 체킹 : 더러워졌는지 를 검사 -> 데이터가 조회를 해오고 : 알아보기
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        // 요청(request) 안에 주문한 상품의 상품번호와, 수량을 Map 형태로 저장되어 있음.
        // ksySet() : 상품 번호 리스트, values() : 수량 리스트
        List<String> productNumbers = new ArrayList<>(request.getProductQuantities().keySet());

        // 위에서 얻은 productNumbers 에 해당하는 product 들을 가져옴.
        List<Product> products = findProductsBy(productNumbers);

        // 재고 관련 있는 타입인 애들 걸러서
        // Product 에서 getProductNumber 값을 추출 (람다 함수)
        // 리스트로
        List<String> stockProductNumbers = products.stream()
                .filter(product -> STOCK_PRODUCT_TYPE.contains(product.getType()))
                .map(Product::getProductNumber)
                .toList();


        // 위에서 걸러낸 상품 번호들에 해당하는 재고(Stock) 객체들을 DB 에서 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);

        // 가져온 stock 들에 대해서
        // productNumber 을 가져오고
        // 요청에서 productNumber 에 해당하는 상품의 수량을 가져옴.
        stocks.forEach(stock -> {
            String stockProductNumber = stock.getProductNumber();
            Integer orderQuantity = request.getProductQuantities().get(stockProductNumber);

            // exception 통과되면
            validateStockQuantity(stock, orderQuantity);

            // 재고 빼기 -> 이후 로직(주문 생성 및 저장) 진행
            stock.reduceQuantity(orderQuantity);
        });

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }

    // 재고 검증 : 재고의 수량이 요청의 수량보다 적으면 에러 throw
    private void validateStockQuantity(Stock stock, Integer orderQuantity) {
        if (stock.getQuantity() < orderQuantity) {
            throw new IllegalArgumentException("재고가 충분하지 않습니다.");
        }
    }
}
