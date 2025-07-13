package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sample.cafekiosk.spring.domain.mail.Mail;
import sample.cafekiosk.spring.domain.mail.MailRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * MailService의 비즈니스 로직을 검증하는 단위 테스트 클래스입니다.
 * Mockito를 사용하여 의존하는 레포지토리를 모킹하고,
 * 매출 요약 메일 전송 시나리오를 검증합니다.
 */
class MailServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MailRepository mailRepository;

    @InjectMocks
    private MailService mailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * MailService의 sendSalesSummaryMail 메서드를 테스트합니다.
     * - 특정 날짜의 결제완료 주문 목록을 조회하고,
     * - 각 주문의 총 매출을 합산한 뒤,
     * - 메일 객체를 저장하는지 검증합니다.
     */
    @Test
    void sendSalesSummaryMail_주문_매출_메일저장_확인() {
        // given: 테스트용 날짜와 주문 데이터를 생성
        LocalDate targetDate = LocalDate.of(2025, 7, 12);

        Product product1 = Product.builder()
                .productNumber("001")
                .name("아메리카노")
                .price(4000)
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .name("카페라떼")
                .price(5000)
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .build();

        Order order1 = new Order(List.of(product1), targetDate.atStartOfDay());
        Order order2 = new Order(List.of(product2), targetDate.atStartOfDay());

        // 주문 레포지토리에서 해당 날짜의 결제완료 주문 목록을 반환하도록 설정
        when(orderRepository.findCompletedOrdersByDate(targetDate))
                .thenReturn(List.of(order1, order2));

        // when: 해당 날짜의 매출 요약 메일 전송 로직 실행
        mailService.sendSalesSummaryMail(targetDate);

        // then: 주문 조회와 메일 저장이 정확히 한 번씩 호출되었는지 검증
        verify(orderRepository, times(1)).findCompletedOrdersByDate(targetDate);
        verify(mailRepository, times(1)).save(any(Mail.class));
    }
}