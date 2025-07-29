package sample.cafekiosk.spring.api.service.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.domain.mail.MailData;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailySalesReportServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private FakeMailService mailService;

    @InjectMocks
    private DailySalesReportService reportService;

    @Test
    void 완료된_주문이_있는_경우_관리자에게_총_매출이_포함된_메일이_전송된다() {
        /*
         * [시나리오 설명]
         * 주어진 날짜(2025-07-27)에 두 건의 완료된 주문이 존재한다.
         * 하나는 5000원, 다른 하나는 7000원으로 총 매출은 12000원이다.
         * 이 경우, 시스템은 해당 매출 합계를 포함한 리포트 메일을
         * 관리자 이메일(admin@cafekiosk.com)로 발송해야 한다.
         */

        // Given: 2025년 7월 27일 완료된 주문이 2건 존재하며, 각각 5000원, 7000원
        LocalDate reportDate = LocalDate.of(2025, 7, 27);
        LocalDateTime start = reportDate.atStartOfDay();
        LocalDateTime end = reportDate.plusDays(1).atStartOfDay();

        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        given(order1.getTotalPrice()).willReturn(5000);
        given(order2.getTotalPrice()).willReturn(7000);

        given(orderRepository.findByRegisteredDateTimeBetweenAndOrderStatus(
                start, end, OrderStatus.COMPLETED
        )).willReturn(List.of(order1, order2));

        ArgumentCaptor<MailData> captor = ArgumentCaptor.forClass(MailData.class);

        // When: 해당 날짜에 대해 매출 리포트 전송을 실행하면
        reportService.sendDailySalesReport(reportDate);

        // Then: 총 12000원이 포함된 메일이 관리자에게 정확히 1번 전송된다
        then(mailService).should(times(1)).sendMail(captor.capture());

        MailData sentMail = captor.getValue();

        assertThat(sentMail.getTo()).isEqualTo("admin@cafekiosk.com");
        assertThat(sentMail.getFrom()).isEqualTo("noreply@cafekiosk.com");
        assertThat(sentMail.getSubject()).isEqualTo("2025-07-27 매출 보고서");
        assertThat(sentMail.getContent()).isEqualTo("총 매출 합계: 12000원");
    }
}

// --------- 미션10 ---------
//class DailySalesReportServiceTest {

//    private OrderRepository orderRepository;
//    private MailHistoryRepository mailHistoryRepository;
//    private FakeMailService mailService;
//    private DailySalesReportService reportService;
//
//    @BeforeEach
//    void setUp() {
//        // Mock 객체 생성
//        orderRepository = mock(OrderRepository.class);
//        mailHistoryRepository = mock(MailHistoryRepository.class);
//        mailService = new FakeMailService(mailHistoryRepository);
//
//        // 테스트 대상 서비스 생성
//        reportService = new DailySalesReportService(orderRepository, mailService);
//    }
//
//    @Test
//    void sendDailySalesReport_sendsMailWithTotalSales() {
//        // given
//        Product product1 = Product.create(
//                "001", "아메리카노", 3000,
//                ProductType.HANDMADE, ProductSellingStatus.SELLING
//        );
//
//        Product product2 = Product.create(
//                "002", "카페라떼", 4000,
//                ProductType.HANDMADE, ProductSellingStatus.SELLING
//        );
//
//        Order order1 = Order.create(
//                List.of(product1),
//                LocalDateTime.of(2025, 7, 14, 10, 0)
//        );
//
//        Order order2 = Order.create(
//                List.of(product2),
//                LocalDateTime.of(2025, 7, 14, 12, 0)
//        );
//
//        List<Order> orders = List.of(order1, order2);
//
//        when(orderRepository.findByRegisteredDateTimeBetweenAndOrderStatus(
//                any(), any(), eq(OrderStatus.COMPLETED)
//        )).thenReturn(orders);
//
//        // when
//        reportService.sendDailySalesReport(LocalDate.of(2025, 7, 14));
//
//        // then
//        verify(mailHistoryRepository, times(1)).save(argThat(mailHistory -> {
//            // 저장된 MailHistory 내용 검증
//            String to = mailHistory.getTo();
//            String from = mailHistory.getFrom();
//            String subject = mailHistory.getSubject();
//            String content = mailHistory.getContent();
//
//            // 총 매출 3000 + 4000 = 7000원
//            return to.equals("admin@cafekiosk.com") &&
//                    from.equals("noreply@cafekiosk.com") &&
//                    subject.contains("2025-07-14 매출 보고서") &&
//                    content.contains("총 매출 합계: 7000");
//        }));
//    }
//}
