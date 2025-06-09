package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.CafekioskApplication;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = CafekioskApplication.class)
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @DisplayName("고객이 선택한 상품 번호 목록으로 주문을 생성하면, 주문 상태는 INIT이고 총 금액이 포함된다")
    @Test
    void createOrder() {
        // given
        Product americano = createProduct("001", "아메리카노", 4000);
        Product latte = createProduct("002", "라떼", 4500);
        productRepository.saveAll(List.of(americano, latte));

        List<String> productNumbers = List.of("001", "002");

        // when
        Order order = orderService.createOrder(productNumbers);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
        assertThat(order.getRegisteredDateTime()).isNotNull();
        assertThat(order.getTotalPrice()).isEqualTo(americano.getPrice() + latte.getPrice());
    }

    private Product createProduct(String productNumber, String name, int price) {
        return new Product(productNumber, name, price, ProductSellingStatus.SELLING);
    }
}
