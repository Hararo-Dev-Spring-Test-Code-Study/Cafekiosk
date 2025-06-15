package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

}