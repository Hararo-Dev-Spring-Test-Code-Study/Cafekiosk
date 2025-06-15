package sample.cafekiosk.spring.domain.stock;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class StockTest {

    @DisplayName("주문 수량만큼 재고를 차감한다")
    @Test
    void deductStock() {
        // given
        Stock stock = Stock.builder()
                .productNumber("001")
                .quantity(10)
                .build();

        // when
        stock.deductQuantity(3);

        // then
        assertThat(stock.getQuantity()).isEqualTo(7);
    }

    @DisplayName("재고보다 더 많은 수량을 주문하면 예외가 발생한다")
    @Test
    void deductStock_withException() {
        // given
        Stock stock = Stock.builder()
                .productNumber("001")
                .quantity(5)
                .build();

        // then
        assertThatThrownBy(() -> stock.deductQuantity(10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }
}
