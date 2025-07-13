package sample.cafekiosk.spring.api.service.mail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.mail.Mail;
import sample.cafekiosk.spring.domain.mail.MailRepository;
import sample.cafekiosk.spring.domain.mail.MailSender;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

@Service
@RequiredArgsConstructor
public class MailService {

  private final MailSender mailSender; // Stub 주입
  private final MailRepository mailRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public void sendMail(String from, String to, String title, String content) {
    mailSender.send(from, to, title, content);

    Mail mail = Mail.builder()
        .fromMail(from)
        .toMail(to)
        .title(title)
        .content(content)
        .build();
    mailRepository.save(mail);
  }

  @Transactional
  public int sendDailySalesMail(LocalDate date, String from, String to) {
    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

    List<Order> orders = orderRepository.findAllByOrderStatusAndRegisteredDateTimeBetween(
        OrderStatus.PAYMENT_COMPLETED, start, end);

    int total = orders.stream()
        .mapToInt(Order::getTotalPrice)
        .sum();

    String title = String.format("일일 매출 보고 - %s", date);
    String content = "총 매출 합계: " + total + "원";

    // 메일 전송 (StubMailSender가 실제 동작)
    mailSender.send(from, to, title, content);

    // 전송 기록 저장 (totalAmount 포함)
    Mail mail = Mail.builder()
        .fromMail(from)
        .toMail(to)
        .title(title)
        .content(content)
        .totalAmount(total)
        .build();
    mailRepository.save(mail);

    return total;
  }
}