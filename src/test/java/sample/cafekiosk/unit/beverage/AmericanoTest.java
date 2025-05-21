package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    void getName() {
        Americano americano=new Americano();

        assertThat(americano.getName().equals("Americano"));
    }

    @Test
    void getPrice() {
        Americano americano=new Americano();

        assertThat(americano.getPrice()==4000);
    }
}