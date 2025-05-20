package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @DisplayName("아메리카노의 이름 확인")
    @Test
    void getName() {
        Americano americano = new Americano();

        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

    @DisplayName("아메리카노의 가격을 확인")
    @Test
    void getPrice() {
        Americano americano = new Americano();

        assertThat(americano.getPrice()).isEqualTo(4000);
    }

}