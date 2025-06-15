package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

// 여기서 dataJpaTest를 진행하면 orderService를 못찾아서 테스트 오류 발생
@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderService orderService;
    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
//        productRepository.deleteAll();
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(BOTTLE, "003", 4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = createStock("001", 10);
        Stock stock2 = createStock("002", 300);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productQuantities(Map.of("001", 10, "002", 300))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        // 주문 ID에 대한 검증
        assertThat(orderResponse.getId()).isNotNull();
        // 실제로 값이 들어가야하는 필드(등록시간, 가격)
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactly(registeredDateTime, 4000);
        // product에 대한 검증
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );
    }

    @DisplayName("재고가 충분하지 않으면 주문 생성 중 예외가 발생한다.")
    @Test
    void createOrder_quantity_exceeded_thenThrowsException() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(BOTTLE, "003", 4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = createStock("001", 10);
        Stock stock2 = createStock("002", 300);
        stockRepository.saveAll(List.of(stock1, stock2));

        // 재고와 관련 있는 상품 타입(002) 를 실제 재고량(300)보다 큰 값(301)으로 설정
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productQuantities(Map.of("001", 10, "002", 301))
                .build();

        // when, then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 충분하지 않습니다.");

    }

    @DisplayName("HANDMADE 타입은 재고가 부족해도 예외가 발생하지 않는다")
    @Test
    void createOrder_handmade_stockExceeded_shouldNotThrowException() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000); // HANDMADE
        productRepository.save(product1);

        // 재고 등록은 했지만, HANDMADE 타입은 무시됨
        Stock stock = createStock("001", 0); // 재고 없음
        stockRepository.save(stock);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productQuantities(Map.of("001", 100)) // 재고 초과 주문
                .build();

        // when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getProducts()).hasSize(1)
                .extracting("productNumber", "price")
                .containsExactly(tuple("001", 1000));
    }

    @DisplayName("주문 생성 시 재고가 차감된다")
    @Test
    void createOrder_stockReduced_success() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product = createProduct(BAKERY, "001", 2000);
        productRepository.save(product);

        Stock stock = createStock("001", 50);
        stockRepository.save(stock);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productQuantities(Map.of("001", 10))
                .build();

        // when
        orderService.createOrder(request, registeredDateTime);

        // then
        Stock updatedStock = stockRepository.findByProductNumber("001");
        assertThat(updatedStock.getQuantity()).isEqualTo(40);
    }


    // 현재는 동일한 상품의 주문이 불가능함..
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productQuantities(Map.of("001", 10, "002", 300,"003", 4000))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        // 주문 ID에 대한 검증
        assertThat(orderResponse.getId()).isNotNull();

        // 실제로 값이 들어가야하는 필드(등록시간, 가격)
        // total price 에 주문 갯수랑 가격이랑 고려해서 계산 해보려 했는데 전두엽 부족해서 안됐음.
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactly(registeredDateTime, 8000);

        // product에 대한 검증
        assertThat(orderResponse.getProducts()).hasSize(3)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 4000)
                );
    }

    // builder 패턴을 사용하여 given에 주어진 정보가 너무 길어 -> 테스트에 필요한 정보와 필요하지 않은 정보를 구분해서
    // 테스트를 깔끔하게 문서 형태로 작성하기 위해 Product를 만드는 도우미 메서드 생성
    private Product createProduct(ProductType type, String ProductNumber, int price ) {
        return Product.builder()
                .type(type)
                .productNumber(ProductNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

    private Stock createStock(String productNumber, int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

}