package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

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

    @DisplayName("")
    @Test
    void remove() {
        // given

        // when

        // then
    }

    @DisplayName("")
    @Test
    void clear() {
        // given

        // when

        // then
    }

    @DisplayName("")
    @Test
    void calculateTotalPrice() {
        // given

        // when

        // then
    }

    @DisplayName("")
    @Test
    void createOrder() {
        // given

        // when

        // then
    }
}