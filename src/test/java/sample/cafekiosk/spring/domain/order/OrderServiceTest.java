package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문 생성 테스트")
    @Test
    void createOrderTest() {

        // given
        Product product1 = productRepository.save(Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(1000)
                .build());

        Product product2 = productRepository.save(Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카페라떼")
                .price(3000)
                .build());

        List<String> productNumbers = List.of("001", "002");

        // when
        Order order = orderService.createOrder(productNumbers);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
        assertThat(order.getRegisteredDateTime()).isNotNull();
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getTotalPrice()).isEqualTo(4000);
    }
}
