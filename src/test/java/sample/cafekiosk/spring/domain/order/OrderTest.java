package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        // Product 객체 2개를 리스트로 생성 -> 각각 1,000원, 2,000원 가격
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        // given절에서 생성한 상품 번호 리스트와 현재시간으로 주문 객체 생성
        Order order = Order.create(products, LocalDateTime.now());

        // then
        // 총금액이 1,000+2,000 = 3,000 되는지 검증
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void init() {
        // given
        // Product 객체 2개 리스트 생성
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        // given절에서 생성한 상품 번호 리스트와 현재시간으로 주문 객체 생성
        Order order = Order.create(products, LocalDateTime.now());

        // then
        // 초기 상태가 INIT인지 확인
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        // 기준시간을 독립적으로 생성해서 저장
        LocalDateTime registeredDateTime = LocalDateTime.now();
        // 상품 객체 리스트 생성
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        // given절에서 생성한 기준시간과 상품 번호 리스트로 주문 객체 생성
        Order order = Order.create(products, registeredDateTime);

        // then
        // 기준시간이 올바르게 저장되었는지 확인
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    // 공통 메서드: Product 생성 도우미
    // 각 테스트에서 중복되는 Product 생성 코드를 줄이기 위한 헬퍼 메서드
    // given절에서 지정하지 않은 type, sellingStatus, name은 이 메서드에서 지정한 빌더 패턴으로 생성됨
    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .type(HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                // 빌더 패턴(Builder Pattern)에서 사용하는 메서드로
                // 객체 생성을 더 읽기 쉽고 명확하게 만들기 위한 방법으로 쓰인다
                // build()를 호출하면 그동안 설정한 필드값을 이용해 최종 객체를 생성한다
                .build();
    }

}