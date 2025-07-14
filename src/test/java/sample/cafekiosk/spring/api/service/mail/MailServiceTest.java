package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.domain.mail.Mail;
import sample.cafekiosk.spring.domain.mail.MailRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

// Mockito를 사용하기 위한 어노테이션
// @Mock, @InjectMocks 어노테이션 동작
@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    //    @Mock - Mockito가 가짜 객체 생성
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailRepository mailRepository;

    //    Mockito가 위의 Mock 객체들을 자동으로 주입해서 MailService 객체를 생성
    @InjectMocks
    private MailService mailService;

    @DisplayName("해당 일자에 결제완료 주문 총 매출을 계산하여 메일을 전송한다")
    @Test
    void sendDailyReport() {
        // given
        // 테스트 데이터(날짜, 주문내역)
        LocalDate date = LocalDate.of(2025, 7, 13);

        List<Order> orders = List.of(
                createOrder(4000),
                createOrder(6000)
        );

//        Stub 설정:
//        orderRepository.findOrdersByStatusAndDate()가 호출되면 위에서 만든 orders 리스트를 반환하도록 지정
//         → 실제 DB를 쓰지 않기 때문에 가짜 응답을 만들어줌
        when(orderRepository.findOrdersByStatusAndRegisteredDateTimeBetween(OrderStatus.PAYMENT_COMPLETED, date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()))
                .thenReturn(orders);

        // when
        mailService.sendDailyReport(date);

        // then
//        orderRepository가 한 번 정확하게 해당 메서드로 호출되었는지를 검증
        verify(orderRepository, times(1)).findOrdersByStatusAndRegisteredDateTimeBetween(OrderStatus.PAYMENT_COMPLETED,
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay());

//        mailRepository.save()가 한 번 호출되었는지 검증
        verify(mailRepository, times(1)).save(any(Mail.class));
    }

    //    진짜 Order 객체를 만들지 않고 Mockito의 mock(Order.class)로 가짜 객체를 생성
    private Order createOrder(int price) {
        Order order = mock(Order.class);
        when(order.getTotalPrice()).thenReturn(price);
        return order;
    }
}
