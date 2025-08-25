package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    /**
     * 모든 ProductType이 text 값을 가지고 있는지 검증
     */
    @ParameterizedTest
    @EnumSource(ProductType.class) // 모든 enum 상수를 순회
    void productType_shouldHaveText(ProductType type) {
        // when
        String text = type.getText();

        // then
        assertThat(text).isNotNull()
                .isNotBlank(); // 빈 문자열이나 null이 아니어야 함
    }

    /**
     * 각 ProductType에 따라 예상되는 text 값이 올바른지 검증
     */
    @ParameterizedTest
    @EnumSource(ProductType.class)
    void productType_textMatch(ProductType type) {
        switch (type) {
            case HANDMADE -> assertThat(type.getText()).isEqualTo("제조 음료");
            case BOTTLE -> assertThat(type.getText()).isEqualTo("병음료");
            case BAKERY -> assertThat(type.getText()).isEqualTo("베이커리");
        }
    }
}