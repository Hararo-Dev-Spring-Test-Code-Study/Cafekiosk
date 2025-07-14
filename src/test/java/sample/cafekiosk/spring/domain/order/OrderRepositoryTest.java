package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private final LocalDate targetDate = LocalDate.of(2025, 7, 13);

    @DisplayName("주어진 날짜와 결제완료 상태에 해당하는 주문만 조회된다")
    @Test
    void findOrdersByStatusAndDate() {
        // given
        Product product = saveProduct();

        Order order1 = Order.create(List.of(product), targetDate.atTime(10, 0));
        order1.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        Order order2 = Order.create(List.of(product), targetDate.atTime(14, 30));
        order2.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        orderRepository.saveAll(List.of(order1, order2));

        // when
        // order1은 2025/7/13 10시, order2는 2025/7/13/ 14시30분에 생성된 주문이고
        // PAYMENT_COMPLETED 결제완료된 주문
        List<Order> result = orderRepository.findOrdersByStatusAndRegisteredDateTimeBetween(
                OrderStatus.PAYMENT_COMPLETED,
                targetDate.atStartOfDay(),
                targetDate.plusDays(1).atStartOfDay()
        );

        // then
        // order1, order2가 조회되기 때문에 size = 2
        assertThat(result).hasSize(2);
    }

    @DisplayName("결제완료 상태가 아닌 주문은 조회되지 않는다")
    @Test
    void findOrdersByStatusAndDate_StatusIsNotPaymentCompleted() {
        // given
        Product product = saveProduct();

        // order1은 OrderStatus를 CANCELED로 설정
        Order order1 = Order.create(List.of(product), targetDate.atTime(9, 0));
        order1.updateOrderStatus(OrderStatus.CANCELED); // 결제완료 아님

        Order order2 = Order.create(List.of(product), targetDate.atTime(14, 30));
        order2.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> result = orderRepository.findOrdersByStatusAndRegisteredDateTimeBetween(
                OrderStatus.PAYMENT_COMPLETED,
                targetDate.atStartOfDay(),
                targetDate.plusDays(1).atStartOfDay()
        );
        // then
        // order1은 결제완료 상태가 아니기 때문에 order2만 조회
        assertThat(result).hasSize(1);
    }

    @DisplayName("다른 날짜의 주문은 조회되지 않는다")
    @Test
    void findOrdersByStatusAndDate_DateDoesNotMatch() {
        // given
        Product product = saveProduct();

        // 2025/7/12에 생성된 주문이기 때문에 targetDate(2025/7/13)이 아님
        Order order1 = Order.create(List.of(product), LocalDate.of(2025, 7, 12).atTime(10, 0));
        order1.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        Order order2 = Order.create(List.of(product), targetDate.atTime(14, 30));
        order2.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> result = orderRepository.findOrdersByStatusAndRegisteredDateTimeBetween(
                OrderStatus.PAYMENT_COMPLETED,
                targetDate.atStartOfDay(),
                targetDate.plusDays(1).atStartOfDay()
        );

        // then
        // order1은 해당 날짜에 생성된 주문이 아니기 때문에 order2만 조회
        assertThat(result).hasSize(1);
    }

    @DisplayName("결제완료 상태가 아니거나 조회 날짜와 일치하지 않는 주문은 조회되지 않는다")
    @Test
    void findOrdersByStatusAndDate_StatusOrDateDoNotMatch() {
        // given
        Product product = saveProduct();

        Order order1 = Order.create(List.of(product), LocalDate.of(2025, 7, 12).atTime(10, 0)); // 다른 날짜
        order1.updateOrderStatus(OrderStatus.PAYMENT_COMPLETED);

        Order order2 = Order.create(List.of(product), targetDate.atTime(14, 30));
        order2.updateOrderStatus(OrderStatus.CANCELED);

        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> result = orderRepository.findOrdersByStatusAndRegisteredDateTimeBetween(
                OrderStatus.PAYMENT_COMPLETED,
                targetDate.atStartOfDay(),
                targetDate.plusDays(1).atStartOfDay()
        );

        // then
        // order1, order2 모두 조건에 맞지 않음. 조회X
        assertThat(result).isEmpty();
    }

    private Product saveProduct() {
        return productRepository.save(
                Product.builder()
                        .productNumber("001")
                        .name("아메리카노")
                        .type(ProductType.HANDMADE)
                        .sellingStatus(ProductSellingStatus.SELLING)
                        .price(5000)
                        .build()
        );
    }
}