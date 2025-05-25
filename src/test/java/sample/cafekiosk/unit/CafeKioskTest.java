package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @DisplayName("고객이 음료를 1잔 추가하면 주문 목록에 해당 음료가 반영되고, 가격 정책에 따라 가격 정보도 함께 반영된다.")
    @Test
    void add() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        // when

        // then
        System.out.println(cafeKiosk.getBeverages().get(0).getName());
        System.out.println(cafeKiosk.getBeverages().get(0).getPrice());
    }

    @DisplayName("음료를 여러 잔 추가하면 각 수량만큼 주문 목록에 추가되어 주문에 반영된다.")
    @Test
    void addSeveralBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.addSeveralBeverages(latte, 3);
        cafeKiosk.addSeveralBeverages(americano, 2);

        // then
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(5);
        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(latte);
        assertThat(cafeKiosk.getBeverages().get(3)).isEqualTo(americano);
    }

    @DisplayName("고객이 음료를 삭제하면 주문 목록에서 해당 음료가 제거되어 최종 주문 내역에 반영되지 않는다.")
    @Test
    void remove() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        // then
        cafeKiosk.remove(latte);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @DisplayName("고객이 전체 주문을 취소하면 주문 목록이 초기화되어 어떤 항목도 포함되지 않는다.")
    @Test
    void clear() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);

        // then
        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalPrice() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        // when
        cafeKiosk.addSeveralBeverages(americano, 3);
        cafeKiosk.addSeveralBeverages(latte, 2);

        // then
        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(21000);
    }

    // LocalDateTime을 외부로 빼내지 않은 상태에서
    // @Test
    //    void createOrder() {
    //        Cafekiosk cafekiosk = new Cafekiosk();
    //        Americano americano = new Americano();
    //        cafekiosk.add(americano);
    //
    //        Order order = cafekiosk.createOrder();
    //
    //        assertThat(order.getBeverages()).hasSize(1);
    //        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    //    }
    // 아래 테스트가 항상 통과 할까?
    // LocalDateTime을 내부에서 사용하면 코드를 작성하는 시간에 따라 테스트 성공 여부가 달라진다.

    @DisplayName("영업 시작 시간 이전에는 주문을 생성할 수 없다.")
    @Test
    void createOrder() {
        // given
        CafeKiosk cafekiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafekiosk.add(americano);

        // when
        Order order = cafekiosk.createOrder(LocalDateTime.of(2025, 3, 3, 10, 0));

        // then
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

        // 예외 상황을 검증하기 위해 사용하는 assertThatThrownBy
        // 예외 상황을 검증하기 위해서는 항상 엣지 케이스 위주로 테스트해보는 것이 좋다
        // 여기에서는 영업시간 직전, 영업시간 종료 직후
        assertThatThrownBy(() -> cafekiosk.createOrder(LocalDateTime.of(2025, 3, 3, 22, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }
}