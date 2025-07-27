package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.order.OrderStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @DisplayName("해당 일자에 결제완료된 주문들을 조회한다.")
    @Test
    void findOrdersBy() {
        // given
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        List<Product> products = List.of(product1, product2);
        productRepository.saveAll(products);

        LocalDateTime startDateTime = LocalDateTime.of(2023, 3, 5, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 6, 0, 0);

        Order order1 = createOrder(LocalDateTime.of(2023, 3, 5, 10, 0), products, PAYMENT_COMPLETED);
        Order order2 = createOrder(LocalDateTime.of(2023, 3, 5, 11, 0), products, PAYMENT_COMPLETED);
        Order order3 = createOrder(LocalDateTime.of(2023, 3, 5, 12, 0), products, INIT);
        Order order4 = createOrder(LocalDateTime.of(2023, 3, 4, 23, 59), products, PAYMENT_COMPLETED);
        Order order5 = createOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products, PAYMENT_COMPLETED);

        orderRepository.saveAll(List.of(order1, order2, order3, order4, order5));

        // when
        List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, PAYMENT_COMPLETED);

        // then
        assertThat(orders).hasSize(2)
                .extracting("orderStatus", "totalPrice")
                .containsExactlyInAnyOrder(
                        tuple(PAYMENT_COMPLETED, 3000),
                        tuple(PAYMENT_COMPLETED, 3000)
                );
    }

    @DisplayName("해당 일자에 결제완료된 주문이 없으면 빈 리스트를 반환한다.")
    @Test
    void findOrdersByEmptyResult() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2023, 3, 5, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 3, 6, 0, 0);

        // when
        List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, PAYMENT_COMPLETED);

        // then
        assertThat(orders).isEmpty();
    }

    private Order createOrder(LocalDateTime registeredDateTime, List<Product> products, OrderStatus orderStatus) {
        Order order = Order.create(products, registeredDateTime);
        order.updateOrderStatus(orderStatus); // Order 엔티티에 추가
        return order;
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .type(HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}