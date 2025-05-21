package sample.cafekiosk.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CafeKioskTest {
    private CafeKiosk cafeKiosk;
    private Americano americano;
    private Latte latte;

    @BeforeEach
    void setUp() {
        cafeKiosk = new CafeKiosk();
        americano = new Americano();
        latte = new Latte();
    }

    @DisplayName("아메리카노 1잔을 추가한다.")
    @Test
    void add() {
        // when
        cafeKiosk.add(americano);

        // then
        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(4000);    // 총 금액 확인
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);   // 주문 개수 확인
        assertThat(cafeKiosk.getBeverages().get(0).getClass()).isEqualTo(Americano.class);  // 첫번째 객체 class 확인
    }

    @DisplayName("아메리카노 3잔, 라떼 2잔을 추가한다.")
    @Test
    void addSeveralBeverages() {
        // when
        cafeKiosk.addSeveralBeverages(americano, 3);    // 4000 * 3 = 12000
        cafeKiosk.addSeveralBeverages(latte, 2);    // 4500 * 2 = 9000

        // then
        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(21000);   // 12000 + 9000 = 21000
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(5);   // 총 음료 5잔 주문
        assertThat(cafeKiosk.getBeverages())
                // AssertJ의 객체의 비교할 필드나 속성 값 설정
                // class로 비교
                .extracting("class")
                // contains 리스트의 객체 class 확인
                .contains(Americano.class, Latte.class)
                // containsExactly class와 순서까지 정확한지 확인
                .containsExactly(Americano.class, Americano.class, Americano.class, Latte.class, Latte.class);
    }

    @DisplayName("아메리카노 한잔을 취소한다.")
    @Test
    void remove() {
        // given
        cafeKiosk.add(americano);   // 4000
        cafeKiosk.addSeveralBeverages(latte, 2);    // 9000

        // when
        cafeKiosk.remove(americano);    // 주문 내역 중 아메리카노 취소

        // then
        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(9000);
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
        assertThat(cafeKiosk.getBeverages())
                // beverage의 이름으로 비교
                .extracting(Beverage::getName)
                .contains("라떼")
                .containsExactly("라떼", "라떼");
    }

    @DisplayName("주문하지 않은 음료를 삭제하면 예외가 발생한다.")
    @Test
    void removeNotOrderedBeverage() {
        // when
        cafeKiosk.add(americano);

        // then
        assertThatThrownBy(() -> cafeKiosk.remove(latte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 하지 않은 음료입니다.");
    }

    @DisplayName("주문을 모두 취소한다.")
    @Test
    void clear() {
        // given
        cafeKiosk.add(americano);   // 4000
        cafeKiosk.addSeveralBeverages(latte, 2);    // 9000

        // when
        cafeKiosk.clear();  // 주문 모두 취소

        // then
        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(0);
        assertThat(cafeKiosk.getBeverages().isEmpty()).isTrue();
    }

    @DisplayName("최종 주문 금액을 확인한다")
    @Test
    void calculateTotalPrice() {
        // given
        cafeKiosk.add(americano);   // 4000
        cafeKiosk.addSeveralBeverages(latte, 2);    // 4500 * 2 = 9000

        // when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(13000);
    }

//    @DisplayName("아메리카노 2잔 주문을 생성한다.")
//    @Test
//    void createOrder() {
//        // given
//        cafeKiosk.addSeveralBeverages(americano, 2);
//
//        // when
//        Order order = cafeKiosk.createOrder();
//
//        // then
//        assertThat(order.getBeverages().size()).isEqualTo(2);
//    }
    @DisplayName("아메리카노 2잔 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        cafeKiosk.addSeveralBeverages(americano, 2);
        LocalDateTime orderTime = LocalDateTime.of(2023, 1, 1, 10, 0);

        // when
        Order order = cafeKiosk.createOrder(orderTime);

        // then
        // order 생성한 시간 확인
        assertThat(order.getOrderDateTime()).isEqualTo(orderTime);
        assertThat(order.getBeverages().size()).isEqualTo(2);
    }
}