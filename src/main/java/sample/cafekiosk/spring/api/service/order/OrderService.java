package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

// Week 7 추가 package
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
// 서비스 전체가 트랜잭션 단위로 실행
// 예 : 주문 중 재고 부족해 예외 발생 시, 주문도 저장되지 않음 -> 롤백 보장
// 실무에서 DB 일관성 위해 중요한 설정
@Transactional

@RequiredArgsConstructor

// 이 클래스가 비지니스 로직을 담은 Service 레이어임을 의미(spring 컴포넌트 스캔 대상)
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    // 재고 정보 DB조회 및 업데이트 위해 추가
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();

        // 기존 findAllByProductNumberIn() 에서 findProductsBy()로 변경
        // findProductsBy() 는 요청 순서를 보장하면서 상품 리스트를 가져옴
        List<Product> products = findProductsBy(productNumbers);

        // 새롭게 추가
        // 상품에 따라 재고 확인 후 차감
        deductStockQuantities(products);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        // 저장된 주문 객체를 기반으로 OrderResponse DTO를 만들어 반환
        // Controller에서 응답으로 내려줄 수 있는 형태로 바꿈
        return OrderResponse.of(savedOrder);
    }

    // findAllByProductNumberIn()은 DB 특성상 순서가 보장되지 않고 IN(...)은 중복허용 안됨
    // ["001", "001", "002"] 요청 시 DB는 ["001", "002"] 만 반환
    // productNumbers 순서대로 상품을 다시 정렬해 반환
    private List<Product> findProductsBy(List<String> productNumbers) {
        // findAllByProductNumberIn(productNumbers)로 DB에서 products 데이터들을 모두 가져옴
        // 이 메서드는 중복제거하고 정렬되지 않은 List<Product> 반환
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // 가져온 product 리스트를 Map<productNumber, Product> 형태로 변환
        // product.steram() : 조회한 Product 리스트 -> Java Stream으로 변환
        Map<String, Product> productMap = products.stream()
                // collect(Collectors.toMap() : 스트림 -> Map 변환
                // Product::getProductNumber : 키로 사용할 값(상품 번호)
                // p -> p : 실제 Product 객체 자체를 값으로 저장
                //{ "001" -> Product("001"), "002" -> Product("002") } 처럼 만들어짐
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        // 원래 입력받은 productNumbers 순서에 따라 productMap에서 다시 Product를 꺼냄
        // 사용자가 실제 요청한 순서와 중복이 포함된 상품번호 리스트 -> 스트림으로 변환
        return productNumbers.stream()
                // 각 상품번호로 productMap에서 대응되는 Product 객체를 가져옴(중복 복구, 순서 유지)
                .map(productMap::get)
                // 최종적으로 다시 List<Product> 형태로 반환
                .collect(Collectors.toList());
    }

    // 재고 관리가 필요한 상품만 필터링
    private static List<String> extractStockProductNumbers(List<Product> products) {
        // products 데이터를 필터링해 재고관리 필요한 productNumber들을 map에 넣어 List를 만들어 반환
        // products 리스트 -> stream 으로 변환
        return products.stream()
                // 재고 필요한 타입의 product만 필터링
                .filter(product -> ProductType.containsStockType(product.getType()))
                // 재고 필요한 Product 객체의 ProductNumber만 추출
                .map(Product::getProductNumber)
                // 추출한 ProductNumber 들을 리스트로 만들어 반환
                .collect(Collectors.toList());
    }

    // 재고 정보를 DB에서 한번에 가져와 Map<String, Stock> 형태로 반환
    // 이후 key로 빠르게 접근 가능
    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        // stockProductNumbers에 해당하는 상품의 재고 정보를 DB에서 한번에 모두 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        // stocks 객체 -> stream으로 변환
        return stocks.stream()
                // stocks의 각 Stock 객체를 productNumber 기준으로 매핑해서 반환
                // 예시
                // 입력 : ["001", "002"]
                // 출력 : { "001" -> Stock("001", 10), "002" -> Stock("002", 3) }
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    }

    // 중복된 상품을 그룹핑하고, 주문된 각 상품의 수량을 계산
    // 예 : ["001", "001", "002"] -> {"001":2, "002":1}
    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        // stockProductNumbers 리스트 -> stream으로 변환
        return stockProductNumbers.stream()
                // 같은 상품번호끼리 그룹화하고 각 상품번호가 몇번 나왔는지 count 해서 반환
                // 입력 : ["001", "001", "002"]
                // 출력 : { "001" -> 2, "002" -> 1 }
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    // 상품에 따라 재고 확인 후 차감하는 메서드
    private void deductStockQuantities(List<Product> products) {
        // 상품들 중 재고가 필요한 상품 타입만 골라서 productNumber 추출
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        // DB에서 stockProductNumbers에 해당하는 재고 조회해서 stockMap에 저장
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        // stockProductNumbers 재고들의 상품별(productNumber별) 수량 계산해 productCountingMap에 저장
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        // 중복된 상품번호는 한 번만 검사하기 위해 HashSet으로 변환
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            // 상품번호로 해당 상품 재고 객체를 가져와 stock에 저장
            Stock stock = stockMap.get(stockProductNumber);
            // 상품번호별 주문 수량 조회해 quantity에 저장
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            // 재고 < 주문수량 이면 예외 발생
            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            // 주문 수량만큼 재고 줄임
            stock.deductQuantity(quantity);
        }
    }

}


// Week 6 OrderService 클래스
//public class OrderService {
//
//    // DB에 접근하는 JPA 레포지토리들
//    private final ProductRepository productRepository;
//    private final OrderRepository orderRepository;
//
//    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
//        // 1. OrderCreateRequest 객체 request에서 사용자가 주문한 상품 번호 목록 추출
//        // 예 : ["001", "002", "003"]
//        List<String> productNumbers = request.getProductNumbers();
//        // 2. DB에서 상품 객체 리스트로 변환
//        // 위에서 추출한 상품 번호들로 실제 상품 객체들을 DB에서 조회
//        // 예 : 상품번호 "001"에 해당하는 Product 엔티티 불러오기
//        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
//
//        // 3. 주문 생성(도메인 로직)
//        // 정적 팩토리 메서드 Order.create()로 주문 객체 생성
//        Order order = Order.create(products, registeredDateTime);
//        // JPA를 통해 실제 DB에 주문 정보 저장
//        Order savedOrder = orderRepository.save(order);
//        // 저장된 주문 객체를 기반으로 OrderResponse DTO를 만들어 반환
//        // Controller에서 응답으로 내려줄 수 있는 형태로 바꿈
//        return OrderResponse.of(savedOrder);
//    }
//
//}
