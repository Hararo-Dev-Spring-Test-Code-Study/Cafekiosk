package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고의 수량이 제공된 수량보다 적은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        // given
        Stock stock = Stock.create("001", 4);
        int quantity = 5;

        // when
        boolean result = stock.isQuantityLessThan(quantity);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
    @Test
    void deductQuantity () {
        // given
        Stock stock = Stock.create("001", 4);
        int quantity = 3;

        // when
        stock.deductQuantity(quantity);

        // then
        assertThat(stock.getQuantity()).isEqualTo(1);
    }

    @TestFactory
    @DisplayName("차감 규칙(정상/경계/예외)을 조합별로 검증한다")
    Collection<DynamicTest> dynamic_deductQuantity() {
        record DeductCase(int initial, int deduct, Integer expectedAfter, Class<? extends Throwable> expectedEx) {}
        List<DeductCase> cases = List.of(
            new DeductCase(4, 3, 1, null),
            new DeductCase(4, 5, null, IllegalArgumentException.class),
            new DeductCase(5, 5, 0, null),
            new DeductCase(10, -1, null, IllegalArgumentException.class)
        );

        return cases.stream()
            .map(c -> DynamicTest.dynamicTest(
                String.format("initial=%d, deduct=%d", c.initial, c.deduct),
                () -> {
                    Stock stock = Stock.create("001", c.initial);

                    if (c.expectedEx == null) {
                        stock.deductQuantity(c.deduct);
                        assertThat(stock.getQuantity()).isEqualTo(c.expectedAfter);
                    } else {
                        assertThatThrownBy(() -> stock.deductQuantity(c.deduct))
                            .isInstanceOf(c.expectedEx)
                            .hasMessage("차감할 재고 수량이 없습니다.");
                    }
                }
            ))
            .toList();
    }

    @DisplayName("재고보다 많은 수량으로 재고 차감을 시도하는 경우 예외가 발생한다.")
    @Test
    void deductQuantity2() {
        // given
        Stock stock = Stock.create("001", 4);
        int quantity = 5;

        // when // Then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 재고 수량이 없습니다.");
    }
}