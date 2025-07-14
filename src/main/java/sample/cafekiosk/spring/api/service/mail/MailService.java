package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.mail.Mail;
import sample.cafekiosk.spring.domain.mail.MailRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailService {

    private final MailRepository mailRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void sendDailyReport(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay(); // 00:00
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay(); // 다음 날 00:00

        List<Order> orders = orderRepository.findOrdersByStatusAndRegisteredDateTimeBetween(
                OrderStatus.PAYMENT_COMPLETED, startDateTime, endDateTime);

        int totalPrice = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        String fromEmail = "cafe@naver.com";
        String toEmail = "admin@naver.com";
        String title = date+"의 총 매출";
        String content = String.format(
                "안녕하세요.\n%s의 총 매출은 %,d원입니다.\n감사합니다.",
                date.toString(), totalPrice
        );

        mailRepository.save(new Mail(fromEmail, toEmail, title, content));
    }
}
