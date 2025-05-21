package sample.cafekiosk.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {


    @DisplayName("음료 1잔 추가 테스트")
    @Test
    void add() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when
        cafeKiosk.add(new Americano());

        // then
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @DisplayName("음료 여러 잔 추가 테스트")
    @Test
    void addSeveralBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when
        //Americano 3번 추가
        cafeKiosk.addSeveralBeverages(new Americano(), 3);

        // then
        //cafeKiosk의 사이즈가 3이 맞는지 확인
        assertThat(cafeKiosk.getBeverages()).hasSize(3);
        // 메뉴확인
        assertThat(cafeKiosk.getBeverages())
                .extracting(beverage -> beverage.getName())
                .containsExactly("Americano", "Americano", "Americano");
    }

    @DisplayName("음료 삭제 테스트")
    @Test
    void remove() {
        // given
        //아메리카노 두개 주문
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.addSeveralBeverages(americano, 2);

        // when
        //아메리카노 1개만 삭제
        cafeKiosk.remove(americano);

        // then
        //아메리카노 1개는 남아있어야 하므로 size 1인지 확인, 항목확인
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        //남은 항목이 아메리카노가 맞는지 확인
        assertThat(cafeKiosk.getBeverages().get(0).getName().equals("Americano"));
    }

    @DisplayName("주문 전체 삭제 테스트")
    @Test
    void clear() {
        // given
        //아메리카노, 라떼 주문
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());

        // when
        //모두 삭제
        cafeKiosk.clear();

        // then
        //주문 비었는지 검증
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @DisplayName("총 금액 계산 테스트")
    @Test
    void calculateTotalPrice() {
        // given
        //아메리카노, 라떼 주문
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano()); // 4000
        cafeKiosk.add(new Latte());     // 4500

        // when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then
        //4000+4500 이므로 8500인지 검증
        assertThat(totalPrice).isEqualTo(8500);
    }

//    @DisplayName("주문 생성 테스트")
//    @Test
//    void createOrder() {
//        // given
//        CafeKiosk cafeKiosk = new CafeKiosk();
//        cafeKiosk.add(new Americano());
//
//        // when
//        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023,1,1,12,30));
//
//        // then
//        //주문 시간
//        assertThat(order.getOrderDateTime());
//
//    }
    @DisplayName("영업시간 외 주문 시 예외 발생")
    @Test
    void createOrder_outsideBusinessHours() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        //영업시간 전 주문 생성
        LocalDateTime beforeOpen = LocalDateTime.of(2023, 1, 1, 9, 0);
        //영업시간 후 주문 생성
        LocalDateTime afterClose = LocalDateTime.of(2023, 1, 1, 22, 30);

        // when & then
        //assertThatThrownBy : 예외 발생 체크
        //isInstaceOf : 발생한 예외의 클래스 타입이 정확한지 확인
        //hasMessage : 예외의 메세지가 정확히 일치하는지 확인
        //유효하지 않은 시간에 주문하면 정확히 예외가 발생하는지 확인
        assertThatThrownBy(() -> cafeKiosk.createOrder(beforeOpen))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 시간이 아닙니다. 영업시간은 10:00~22:00입니다.");

        assertThatThrownBy(() -> cafeKiosk.createOrder(afterClose))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 시간이 아닙니다. 영업시간은 10:00~22:00입니다.");
    }
}