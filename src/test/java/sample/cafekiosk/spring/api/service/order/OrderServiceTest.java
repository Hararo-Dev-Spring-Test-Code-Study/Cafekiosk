package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

// 여기서 dataJpaTest를 진행하면 orderService를 못찾아서 테스트 오류 발생
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {

        //productRepository.deleteAll();
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002"));

        // when
        ApiResponse<OrderResponse> response = orderService.createOrder(request, registeredDateTime);
        OrderResponse orderResponse = response.getData();

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

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001"));

        // when
        ApiResponse<OrderResponse> response = orderService.createOrder(request, registeredDateTime);
        OrderResponse orderResponse = response.getData();

        // then
        // 주문 ID에 대한 검증
        assertThat(orderResponse.getId()).isNotNull();
        // 실제로 값이 들어가야하는 필드(등록시간, 가격)
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactly(registeredDateTime, 2000);
        // product에 대한 검증
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }

    @DisplayName("주문하는 상품의 재고를 확인하고 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001", "002"));

        // when
        ApiResponse<OrderResponse> response = orderService.createOrder(request, registeredDateTime);
        OrderResponse orderResponse = response.getData();

        // then
        assertThat(orderResponse.getId()).isNotNull();

        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactly(registeredDateTime, 5000);

        assertThat(orderResponse.getProducts()).hasSize(3)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 0)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 주문할 수 없다.")
    @Test
    void createOrderWithNoStock() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 1);
        stock1.deductQuantity(1); // todo
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001", "002", "003"));

        // when // then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");

    }

    // builder 패턴을 사용하여 given에 주어진 정보가 너무 길어 -> 테스트에 필요한 정보와 필요하지 않은 정보를 구분해서
    // 테스트를 깔끔하게 문서 형태로 작성하기 위해 Product를 만드는 도우미 메서드 생성
    private Product createProduct(ProductType type, String ProductNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(ProductNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}