package sample.cafekiosk.spring.api.service.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sample.cafekiosk.spring.domain.mail.MailHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.infra.mail.FakeMailService;
import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DailySalesReportServiceTest {

    private OrderRepository orderRepository;
    private MailHistoryRepository mailHistoryRepository;
    private FakeMailService mailService;
    private DailySalesReportService reportService;

    @BeforeEach
    void setUp() {
        // Mock 객체 생성
        orderRepository = mock(OrderRepository.class);
        mailHistoryRepository = mock(MailHistoryRepository.class);
        mailService = new FakeMailService(mailHistoryRepository);

        // 테스트 대상 서비스 생성
        reportService = new DailySalesReportService(orderRepository, mailService);
    }

    @Test
    void sendDailySalesReport_sendsMailWithTotalSales() {
        // given
        Product product1 = Product.create(
                "001", "아메리카노", 3000,
                ProductType.HANDMADE, ProductSellingStatus.SELLING
        );

        Product product2 = Product.create(
                "002", "카페라떼", 4000,
                ProductType.HANDMADE, ProductSellingStatus.SELLING
        );

        Order order1 = Order.create(
                List.of(product1),
                LocalDateTime.of(2025, 7, 14, 10, 0)
        );

        Order order2 = Order.create(
                List.of(product2),
                LocalDateTime.of(2025, 7, 14, 12, 0)
        );

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findByRegisteredDateTimeBetweenAndOrderStatus(
                any(), any(), eq(OrderStatus.COMPLETED)
        )).thenReturn(orders);

        // when
        reportService.sendDailySalesReport(LocalDate.of(2025, 7, 14));

        // then
        verify(mailHistoryRepository, times(1)).save(argThat(mailHistory -> {
            // 저장된 MailHistory 내용 검증
            String to = mailHistory.getTo();
            String from = mailHistory.getFrom();
            String subject = mailHistory.getSubject();
            String content = mailHistory.getContent();

            // 총 매출 3000 + 4000 = 7000원
            return to.equals("admin@cafekiosk.com") &&
                    from.equals("noreply@cafekiosk.com") &&
                    subject.contains("2025-07-14 매출 보고서") &&
                    content.contains("총 매출 합계: 7000");
        }));
    }
}
