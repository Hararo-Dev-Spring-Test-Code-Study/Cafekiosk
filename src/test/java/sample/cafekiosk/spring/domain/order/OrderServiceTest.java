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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@SpringBootTest // DataJpaTest는 Service 로딩X
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

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

//        Product p2 = Product.builder()
//                .productNumber("002")
//                .type(HANDMADE)
//                .sellingStatus(HOLD)
//                .name("카페라떼")
//                .price(4500)
//                .build();

        Product p3 = Product.builder()
                .productNumber("003")
                .type(BAKERY)
                .sellingStatus(STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

//        productRepository.saveAll(List.of(p1, p2, p3));
        productRepository.saveAll(List.of(p1, p3));

        List<String> productNumbers = List.of(p1.getProductNumber(), p3.getProductNumber());
        LocalDateTime orderTime = LocalDateTime.of(2025, 6, 9, 10, 0);

        //when
        OrderResponse response = orderService.createOrder(productNumbers, orderTime);

        //then
        assertThat(response.getTotalPrice()).isEqualTo(7500);
        assertThat(response.getProducts())
                .hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("003", "크루아상", 3500)
                );
    }
}