package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ExtendWith(MockitoExtension.class)
class OrderStaticsServiceMockTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private OrderStaticsService orderStaticsService;

    @DisplayName("결제가 완료된 주문을 조회하여 매출 통계를 이메일로 전송한다.")
    @Test
    void sendOrderStatics() {
        // given
        LocalDate orderDate = LocalDate.of(2025, 7, 14);
        String email = "test@test.com";

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);

        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2025, 7, 14, 10, 0));
        Order order2 = createPaymentCompletedOrder(products, LocalDateTime.of(2025, 7, 14, 15, 0));
        List<Order> orders = List.of(order1, order2);

        given(orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        )).willReturn(orders);

        given(mailService.sendMail(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).willReturn(true);

        // when
        boolean result = orderStaticsService.sendOrderStatics(orderDate, email);

        // then
        assertThat(result).isTrue();

        then(orderRepository).should(times(1)).findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        then(mailService).should(times(1)).sendMail(
                "no-reply@test.com",
                email,
                "[매출통계] 2025-07-14",
                "총 매출 합계는 12000원입니다."
        );
    }

    @DisplayName("메일 전송이 실패하면 예외가 발생한다.")
    @Test
    void sendOrderStaticsWhenMailSendFails() {
        // given
        LocalDate orderDate = LocalDate.of(2025, 7, 14);
        String email = "test@test.com";

        Product product = createProduct(HANDMADE, "001", 1000);
        List<Product> products = List.of(product);
        Order order = createPaymentCompletedOrder(products, LocalDateTime.of(2025, 7, 14, 10, 0));
        List<Order> orders = List.of(order);

        given(orderRepository.findOrdersBy(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(OrderStatus.class)
        )).willReturn(orders);

        given(mailService.sendMail(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).willReturn(false);

        // when & then
        assertThatThrownBy(() -> orderStaticsService.sendOrderStatics(orderDate, email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("매출 통계 메일 전송에 실패했습니다.");
    }

    @DisplayName("주문이 없는 날의 매출 통계를 전송한다.")
    @Test
    void sendOrderStaticsWithNoOrders() {
        // given
        LocalDate orderDate = LocalDate.of(2025, 7, 14);
        String email = "test@test.com";
        List<Order> emptyOrders = List.of();

        given(orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        )).willReturn(emptyOrders);

        given(mailService.sendMail(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        )).willReturn(true);

        // when
        boolean result = orderStaticsService.sendOrderStatics(orderDate, email);

        // then
        assertThat(result).isTrue();

        then(mailService).should(times(1)).sendMail(
                "no-reply@test.com",
                email,
                "[매출통계] 2025-07-14",
                "총 매출 합계는 0원입니다."
        );
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime registeredDateTime) {
        return Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(registeredDateTime)
                .build();
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}
