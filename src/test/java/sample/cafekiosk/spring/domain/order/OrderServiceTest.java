package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@SpringBootTest // DataJpaTest는 Service 로딩X
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;

    @DisplayName("상품 번호 리스트를 받아 주문을 생성하고 총 금액이 계산된다")
    @Test
    void calculateTotalPrice() {
        //given
        Product p1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product p2 = Product.builder()
                .productNumber("003")
                .type(BAKERY)
                .sellingStatus(STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

        productRepository.saveAll(List.of(p1, p2));

        // BAKERY 상품 재고 확인
        stockRepository.save(
                Stock.builder()
                        .productNumber("003")
                        .quantity(5)
                        .build()
        );

        // 상품 번호 + 수량으로 변경
        Map<String, Integer> productNumberWithQuantity = Map.of(
                p1.getProductNumber(), 1,
                p2.getProductNumber(), 1
        );
        LocalDateTime orderTime = LocalDateTime.of(2025, 6, 9, 10, 0);

        //when
        OrderResponse response = orderService.createOrder(productNumberWithQuantity, orderTime);

        //then
        assertThat(response.getTotalPrice()).isEqualTo(7500);
        assertThat(response.getProducts())
                .hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("003", "크루아상", 3500)
                );
        assertThat(response.getOrderDateTime()).isEqualTo(orderTime);
        assertThat(response.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @DisplayName("주문을 생성할 때 상품의 재고가 차감된다")
    @Test
    void createOrder_deductStock() {
        // given
        Product product1 = productRepository.save(
                Product.builder()
                        .productNumber("001")
                        .type(BAKERY)
                        .sellingStatus(SELLING)
                        .name("크루아상")
                        .price(3000)
                        .build()
        );

        Product product2 = productRepository.save(
                Product.builder()
                        .productNumber("004")
                        .type(BOTTLE)
                        .sellingStatus(SELLING)
                        .name("사과주스")
                        .price(5000)
                        .build()
        );

        stockRepository.saveAll(List.of(
                Stock.builder()
                        .productNumber("001")
                        .quantity(5)
                        .build(),
                Stock.builder()
                        .productNumber("004")
                        .quantity(10)
                        .build()
        ));

        Map<String, Integer> orderRequest = Map.of("001", 3, "004", 8);
        LocalDateTime orderTime = LocalDateTime.of(2025, 6, 15, 10, 0);

        // when
        orderService.createOrder(orderRequest, orderTime);

        // then
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "004"));
        assertThat(stocks).hasSize(2);
        assertThat(stocks)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 2),
                        tuple("004", 2)
                );
    }

    @DisplayName("주문을 생성할 때 상품의 재고보다 많은 수량을 주문하면 예외가 발생한다")
    @Test
    void createOrder_deductStock_withException() {
        // given
        Product product1 = productRepository.save(
                Product.builder()
                        .productNumber("001")
                        .type(BAKERY)
                        .sellingStatus(SELLING)
                        .name("크루아상")
                        .price(3000)
                        .build()
        );

        Product product2 = productRepository.save(
                Product.builder()
                        .productNumber("004")
                        .type(BOTTLE)
                        .sellingStatus(SELLING)
                        .name("사과주스")
                        .price(5000)
                        .build()
        );

        stockRepository.saveAll(List.of(
                Stock.builder()
                        .productNumber("001")
                        .quantity(2)
                        .build(),
                Stock.builder()
                        .productNumber("004")
                        .quantity(1)
                        .build()
        ));

        Map<String, Integer> orderRequest = Map.of("001", 3, "004", 1);
        LocalDateTime orderTime = LocalDateTime.of(2025, 6, 15, 10, 0);


        // then
        assertThatThrownBy(() -> orderService.createOrder(orderRequest, orderTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }
}