package sample.cafekiosk.spring.domain.product;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @Test
    void containsStockType() {
        // given
        ProductType givenType = ProductType.HANDMADE;
        ProductType givenType2 = ProductType.BOTTLE;

        // when
        boolean result = ProductType.containsStockType(givenType);
        boolean result2 = ProductType.containsStockType(givenType2);

        // then
        assertThat(result).isFalse();
        assertThat(result2).isTrue();
    }


    @DisplayName("재고와 연동되는 타입이면 containsStockType()이 true 여야 한다")
    @ParameterizedTest(name = "{index} => type={0}")
    @EnumSource(value = ProductType.class, names = {"BOTTLE", "BAKERY"})
    void containsStockType_shouldBeTrue(ProductType type) {
        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고와 연동되지 않는 타입이면 containsStockType()이 false 여야 한다")
    @ParameterizedTest(name = "{index} => type={0}")
    @EnumSource(value = ProductType.class, names = {"HANDMADE"})
    void containsStockType_shouldBeFalse(ProductType type) {
        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("재고와 연동되지 않는 타입이면 containsStockType()이 false 여야 한다 - ValueSource 버전")
    @ParameterizedTest(name = "{index} => typeName={0}")
    @ValueSource(strings = {"BOTTLE", "BAKERY"})
    void containsStockType_shouldBeFalse_withValueSource(String typeName) {
        // given
        ProductType type = ProductType.valueOf(typeName);

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("각 타입의 표시 텍스트(expectedText)가 일치해야 한다")
    @ParameterizedTest(name = "{index} => type={0}, expectedText={1}")
    @CsvSource({
        "HANDMADE, 제조 음료",
        "BOTTLE, 병음료",
        "BAKERY, 베이커리"
    })
    void labelText(ProductType type, String expectedText) {
        assertThat(type.getText()).isEqualTo(expectedText);
    }


    // 재고 연동되는 타입들을 리턴하는 함수
    static Stream<Arguments> stockCases() {
        return Stream.of(
            Arguments.of(ProductType.BOTTLE, true),
            Arguments.of(ProductType.HANDMADE, false)
        );
    }

    @ParameterizedTest
    @MethodSource("stockCases")
    @DisplayName("재고 연동 여부를 확인한다")
    void containsStockType(ProductType type, boolean expected) {
        assertThat(ProductType.containsStockType(type)).isEqualTo(expected);
    }

}