package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @AfterEach
    void tearDown() {
        mailSendHistoryRepository.deleteAllInBatch();
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);

        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        Product product3 = createProduct("003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 10, 0), products);
        Order order2 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 11, 0), products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 12, 0), products);
        orderRepository.saveAll(List.of(order1, order2, order3));

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(
                LocalDate.of(2023, 3, 5),
                "test@test.com"
        );

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("fromEmail", "toEmail", "subject", "content")
                .containsExactlyInAnyOrder(
                        tuple("no-reply@cafekiosk.com", "test@test.com", "[매출통계] 2023-03-05", "총 매출 합계는 18000원입니다.")
                );
    }

    @DisplayName("결제완료 주문이 없는 경우 0원으로 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMailNoOrders() {
        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(
                LocalDate.of(2023, 3, 5),
                "test@test.com"
        );

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("fromEmail", "toEmail", "subject", "content")
                .containsExactlyInAnyOrder(
                        tuple("no-reply@cafekiosk.com", "test@test.com", "[매출통계] 2023-03-05", "총 매출 합계는 0원입니다.")
                );
    }

    @DisplayName("결제완료가 아닌 주문은 매출 통계 대상에서 제외된다.")
    @Test
    void sendOrderStatisticsMailExcludingNonPaymentCompletedOrders() {
        // given
        Product product1 = createProduct("001", 1000);
        List<Product> products = List.of(product1);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 10, 0), products);
        Order order2 = createOrder(LocalDateTime.of(2023, 3, 5, 11, 0), products, OrderStatus.INIT);
        Order order3 = createOrder(LocalDateTime.of(2023, 3, 5, 12, 0), products, OrderStatus.CANCELED);
        orderRepository.saveAll(List.of(order1, order2, order3));

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(
                LocalDate.of(2023, 3, 5),
                "test@test.com"
        );

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .containsExactlyInAnyOrder("총 매출 합계는 1000원입니다.");
    }

    @DisplayName("해당 날짜의 주문만 매출 통계 대상에 포함된다.")
    @Test
    void sendOrderStatisticsMailOnlyIncludeTargetDate() {
        // given
        Product product1 = createProduct("001", 1000);
        List<Product> products = List.of(product1);
        productRepository.saveAll(products);

        Order orderToday = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59), products);
        Order orderYesterday = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59), products);
        Order orderTomorrow = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);
        orderRepository.saveAll(List.of(orderToday, orderYesterday, orderTomorrow));

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(
                LocalDate.of(2023, 3, 5),
                "test@test.com"
        );

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .containsExactlyInAnyOrder("총 매출 합계는 1000원입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime registeredDateTime, List<Product> products) {
        return createOrder(registeredDateTime, products, OrderStatus.PAYMENT_COMPLETED);
    }

    private Order createOrder(LocalDateTime registeredDateTime, List<Product> products, OrderStatus orderStatus) {
        Order order = Order.create(products, registeredDateTime);
        order.updateOrderStatus(orderStatus); // Order 엔티티에 추가 필요
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