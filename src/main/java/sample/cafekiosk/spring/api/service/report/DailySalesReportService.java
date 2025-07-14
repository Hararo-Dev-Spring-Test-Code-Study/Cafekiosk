package sample.cafekiosk.spring.api.service.report;

import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.mail.MailData;
import sample.cafekiosk.spring.infra.mail.FakeMailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailySalesReportService {

    private final OrderRepository orderRepository;
    private final FakeMailService mailService;

    public DailySalesReportService(OrderRepository orderRepository, FakeMailService mailService) {
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    public void sendDailySalesReport(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        List<Order> orders = orderRepository.findByRegisteredDateTimeBetweenAndOrderStatus(
                startDateTime,
                endDateTime,
                OrderStatus.COMPLETED
        );

        int totalSales = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        String content = "총 매출 합계: " + totalSales + "원";

        MailData mailData = new MailData(
                "noreply@cafekiosk.com",
                "admin@cafekiosk.com",
                date + " 매출 보고서",
                content
        );

        mailService.sendMail(mailData);
    }
}
