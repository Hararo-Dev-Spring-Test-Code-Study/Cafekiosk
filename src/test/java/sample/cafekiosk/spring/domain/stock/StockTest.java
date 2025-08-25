package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고 차감 동작 테스트 - 다양한 수량")
    @TestFactory
    List<DynamicTest> deductStockDynamically() {
        return IntStream.of(1, 2, 3, 4, 5)
                .mapToObj(deductQuantity ->
                        DynamicTest.dynamicTest("재고 10에서 " + deductQuantity + "개 차감",
                                () -> {
                                    // given
                                    Stock stock = new Stock("001", 10);

                                    // when
                                    stock.deduct(deductQuantity);

                                    // then
                                    assertThat(stock.getQuantity()).isEqualTo(10 - deductQuantity);
                                })
                ).collect(Collectors.toList());
    }

    @DisplayName("재고 차감 실패 테스트 - 수량 초과")
    @TestFactory
    List<DynamicTest> deductFailWhenOver() {
        return IntStream.of(11, 20)
                .mapToObj(deductQuantity ->
                        DynamicTest.dynamicTest("재고 10에서 " + deductQuantity + "개 차감 시 예외 발생",
                                () -> {
                                    // given
                                    Stock stock = new Stock("001", 10);

                                    // when & then
                                    assertThatThrownBy(() -> stock.deduct(deductQuantity))
                                            .isInstanceOf(IllegalArgumentException.class)
                                            .hasMessage("재고가 부족합니다.");
                                })
                ).collect(Collectors.toList());
    }
}