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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatisticsServiceMockTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private OrderStatisticsService orderStatisticsService;

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다 - Mock 기반")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDate orderDate = LocalDate.of(2023, 3, 5);
        String email = "test@test.com";

        Order order1 = createMockOrder(6000);
        Order order2 = createMockOrder(4000);
        Order order3 = createMockOrder(8000);
        List<Order> orders = List.of(order1, order2, order3);

        when(orderRepository.findOrdersBy(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(OrderStatus.class)
        )).thenReturn(orders);

        when(mailService.sendMail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(orderDate, email);

        // then
        assertThat(result).isTrue();
        verify(orderRepository, times(1)).findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );
        verify(mailService, times(1)).sendMail(
                "no-reply@cafekiosk.com",
                email,
                "[매출통계] 2023-03-05",
                "총 매출 합계는 18000원입니다."
        );
    }

    @DisplayName("결제완료 주문이 없는 경우 0원으로 매출 통계 메일을 전송한다 - Mock 기반")
    @Test
    void sendOrderStatisticsMailNoOrders() {
        // given
        LocalDate orderDate = LocalDate.of(2023, 3, 5);
        String email = "test@test.com";

        when(orderRepository.findOrdersBy(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(OrderStatus.class)
        )).thenReturn(List.of());

        when(mailService.sendMail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(orderDate, email);

        // then
        assertThat(result).isTrue();
        verify(mailService, times(1)).sendMail(
                "no-reply@cafekiosk.com",
                email,
                "[매출통계] 2023-03-05",
                "총 매출 합계는 0원입니다."
        );
    }

    private Order createMockOrder(int totalPrice) {
        Order order = mock(Order.class);
        when(order.getTotalPrice()).thenReturn(totalPrice);
        return order;
    }

}