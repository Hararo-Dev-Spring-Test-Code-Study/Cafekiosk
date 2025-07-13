package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.domain.mail.MailRepository;
import sample.cafekiosk.spring.domain.mail.MailSender;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

  @Mock
  MailSender mailSender;

  @Mock
  MailRepository mailRepository;

  @Mock
  OrderRepository orderRepository;

  @InjectMocks
  MailService mailService;

  @Test
  @DisplayName("메일 발송 시 메일 전송 및 저장이 정상적으로 이루어진다")
  void sendMail() {
    // when
    mailService.sendMail("from@from.from", "to@to.to",
        "title", "content/content");

    // then : MailSender 호출 검증
    verify(mailSender).send(
        eq("from@from.from"),
        eq("to@to.to"),
        eq("title"),
        argThat(body -> body.contains("content/content"))
    );

    // then : 저장 호출 검증
    verify(mailRepository).save(any());
  }

  @Test
  @DisplayName("일일 매출 메일 전송 시 총합 계산 후 메일 전송 및 저장된다")
  void sendDailySalesMail() {
    // given
    LocalDate date = LocalDate.of(2025, 7, 14);
    Order order1 = org.mockito.Mockito.mock(Order.class);
    when(order1.getTotalPrice()).thenReturn(4000);
    Order order2 = org.mockito.Mockito.mock(Order.class);
    when(order2.getTotalPrice()).thenReturn(6000);

    when(orderRepository.findAllByOrderStatusAndRegisteredDateTimeBetween(
        eq(OrderStatus.PAYMENT_COMPLETED),
        any(LocalDateTime.class),
        any(LocalDateTime.class)))
        .thenReturn(List.of(order1, order2));

    // when
    int total = mailService.sendDailySalesMail(date, "from@from.from", "to@to.to");
    String title = String.format("일일 매출 보고 - %s", date);

    // then
    assertThat(total).isEqualTo(10000);
    verify(mailSender).send(
        eq("from@from.from"),
        eq("to@to.to"),
        eq(title),
        argThat(content -> content.contains("10000"))
    );
    verify(mailRepository).save(argThat(m -> m.getTotalAmount() == 10000));
  }

  @Test
  @DisplayName("결제완료 주문이 0건이면 합계 0원으로 메일 전송·저장된다")
  void sendDailySalesMail_zeroOrders() {
    // given
    LocalDate date = LocalDate.of(2025, 7, 15);
    when(orderRepository.findAllByOrderStatusAndRegisteredDateTimeBetween(
        eq(OrderStatus.PAYMENT_COMPLETED), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(List.of());

    // when
    int total = mailService.sendDailySalesMail(date, "from@from.from", "to@to.to");
    String title = String.format("일일 매출 보고 - %s", date);

    // then
    assertThat(total).isZero();
    verify(mailSender).send(
        eq("from@from.from"),
        eq("to@to.to"),
        eq(title),
        eq("총 매출 합계: 0원")
    );
    verify(mailRepository).save(argThat(m -> m.getTotalAmount() == 0));
  }

}