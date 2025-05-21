package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CafeKioskTest {

    @DisplayName("음료 1잔을 추가한다.")
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

    @DisplayName("음료를 여러 잔 추가한다.")
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

    @DisplayName("가장 최근에 주문한 주문내용을 삭제한다.")
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

    @DisplayName("주문 목록 전체를 삭제한다.")
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

    @DisplayName("주문한 음료의 총 금액을 계산한다.")
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

    @DisplayName("주문을 생성한 시간이 주문 시간 이내인지 확인한다.")
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
    }
}