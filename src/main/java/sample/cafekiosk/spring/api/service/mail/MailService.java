package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.mail.Mail;
import sample.cafekiosk.spring.domain.mail.MailRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {

    private final OrderRepository orderRepository;
    private final MailRepository mailRepository;

    public void sendSalesSummaryMail(LocalDate targetDate) {
        List<Order> completedOrders = orderRepository.findCompletedOrdersByDate(targetDate);

        int totalSales = completedOrders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        String subject = "[매출 요약] " + targetDate;
        String content = "총 매출 합계는 " + totalSales + "원입니다.";

        Mail mail = new Mail();
        mail.setFromEmail("admin@example.com");
        mail.setToEmail("sales@example.com");
        mail.setSubject(subject);
        mail.setContent(content);
        mail.setSentAt(LocalDateTime.now());

        mailRepository.save(mail);
        System.out.println("메일 전송 기록 저장됨: " + subject);
    }
}
