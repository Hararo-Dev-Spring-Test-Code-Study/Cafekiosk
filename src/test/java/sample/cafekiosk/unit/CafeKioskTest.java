package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CafeKioskTest {

    @DisplayName("음료 1개를 추가하면 리스트에 1개가 담긴다.")
    @Test
    void add() {
        // given
        CafeKiosk kiosk = new CafeKiosk();

        // when
        kiosk.add(new Americano());

        // then
        assertThat(kiosk.getBeverages()).hasSize(1);
        assertThat(kiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @DisplayName("같은 음료를 여러 잔 추가하면 해당 수만큼 리스트에 담긴다.")
    @Test
    void addSeveralBeverages() {
        // given
        CafeKiosk kiosk = new CafeKiosk();

        // when
        kiosk.addSeveralBeverages(new Latte(), 3);

        // then
        assertThat(kiosk.getBeverages()).hasSize(3);
        assertThat(kiosk.getBeverages().get(0).getName()).isEqualTo("Latte");
    }

    @DisplayName("리스트에서 음료 1개를 삭제할 수 있다.")
    @Test
    void remove() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        Latte latte = new Latte();
        kiosk.add(latte);

        // when
        kiosk.remove(latte);

        // then
        assertThat(kiosk.getBeverages()).isEmpty();
    }

    @DisplayName("전체 음료를 삭제하면 리스트가 비워진다.")
    @Test
    void clear() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when
        kiosk.clear();

        // then
        assertThat(kiosk.getBeverages()).isEmpty();
    }

    @DisplayName("총 금액을 계산하면 음료 가격의 합이 반환된다.")
    @Test
    void calculateTotalPrice() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano()); // 4000
        kiosk.add(new Latte());     // 4500

        // when
        int totalPrice = kiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(8500);
    }

    @DisplayName("주문을 생성하면 주문 시간과 음료 목록이 포함된다.")
    @Test
    void createOrder() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 10, 0);

        // when
        Order order = kiosk.createOrder(time);

        // then
        assertThat(order.getOrderDateTime()).isEqualTo(time);
        assertThat(order.getBeverages()).hasSize(1);
    }
}
