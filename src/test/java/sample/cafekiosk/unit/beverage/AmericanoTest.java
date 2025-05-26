package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

//    @DisplayName("아메리카노의 getName()은 아메리카노 반환한다")
    @DisplayName("Ameericano 메뉴의 이름은 '아메리카노' 이다.")
    @Test
    void getName() {
        // given
        Americano americano = new Americano();

        // when
        String name = americano.getName();

        // then
        assertThat(name).isEqualTo("아메리카노");
    }

//    @DisplayName("아메리카노의 getPrice()는 4000반환한다")
    @DisplayName("Americano 메뉴의 가격은 4000 원이다.")
    @Test
    void getPrice() {
        // given
        Americano americano = new Americano();

        // when
        int price = americano.getPrice();

        // then
        assertThat(price).isEqualTo(4000);
    }

}