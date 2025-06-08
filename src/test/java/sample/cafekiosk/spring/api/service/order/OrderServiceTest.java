package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.DisplayName; // 테스트 이름을 지정해줌 (콘솔에 예쁘게 출력)
import org.junit.jupiter.api.Test; // 테스트 메서드로 인식되게 함
import org.springframework.beans.factory.annotation.Autowired; // 필요한 의존성 주입
import org.springframework.boot.test.context.SpringBootTest; // 스프링 전체 빈을 띄우고 통합 테스트
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.transaction.Transactional; // 테스트가 끝난 뒤 롤백되도록
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // DB 에 저장하는 로직이 있음 -> 각 테스트마다 트랜잭션 적용 후 롤백
@ActiveProfiles("test") // application-test.yml 을 사용하기 위해

class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문은 유효한 상품 목록을 통해 생성되며, 초기 상태는 '접수 대기(PENDING)'이다")
    @Test
    void createOrder() {
        // given - 주문할 상품들을 DB에 미리 저장
        Product product1 = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000).build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("카페라떼")
                .price(4500).build();

        // createOrder 가 Product를 DB 에서 조회하기 때문에 저장 먼저 해야 함
        productRepository.saveAll(List.of(product1, product2));

        List<String> productNumbers = List.of("001", "002");

        // when - 001, 002 로 주문 생성 로직을 호출
        Order order = orderService.createOrder(productNumbers);

        // then - 주문이 정상적으로 생성됐는지 검증
        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }


    @DisplayName("주문 요청에 존재하지 않는 상품 번호가 포함된 경우, 주문 생성은 실패한다")
    @Test
    void createOrderWithInvalidProductNumber() {
        // given - 존재하는 상품은 하나만 저장
        Product product = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        productRepository.save(product);

        List<String> productNumbers = List.of("001", "999"); // 999는 존재하지 않음

        // when, then
        assertThatThrownBy(() -> orderService.createOrder(productNumbers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 번호 중 일치하는 상품이 없는 번호가 있습니다.");
    }


    @DisplayName("주문에 포함된 상품들의 총 주문 금액을 계산할 수 있다")
    @Test
    void calculateTotalPrice() {
        // given - 상품을 저장하고 주문을 생성
        Product product1 = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000).build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("카페라떼")
                .price(4500).build();

        productRepository.saveAll(List.of(product1, product2));
        Order order = orderService.createOrder(List.of("001", "002"));

        // when - 총 금액 계산 메서드를 호출
        int totalPrice = orderService.getTotalPrice(order);

        // then - 예상한 총 금액과 일치하는지 검증
        assertThat(totalPrice).isEqualTo(8500);
    }
}
