package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    @DisplayName("아메리카노는 메뉴명은 'Americano'로 설정되어 있다")
    void getName() {
        Americano americano=new Americano();

        assertThat(americano.getName().equals("Americano"));
    }

    @Test
    @DisplayName("아메리카노는 4000원으로 제공된다")
    void getPrice() {
        Americano americano=new Americano();

        assertThat(americano.getPrice()==4000);
    }
}