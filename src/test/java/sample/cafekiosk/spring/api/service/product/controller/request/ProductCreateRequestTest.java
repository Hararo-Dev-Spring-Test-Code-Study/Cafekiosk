package sample.cafekiosk.spring.api.service.product.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCreateRequestTest {

    @Test
    @DisplayName("ProductCreateRequest가 정상적으로 생성되고 값을 반환한다")
    void createProductRequest() {
        // given
        String name = "콜드브루";
        ProductType type = ProductType.HANDMADE;
        ProductSellingStatus status = ProductSellingStatus.SELLING;
        int price = 4800;

        // when
        ProductCreateRequest request = new ProductCreateRequest(name, type, status, price);

        // then
        assertThat(request.getName()).isEqualTo("콜드브루");
        assertThat(request.getType()).isEqualTo(ProductType.HANDMADE);
        assertThat(request.getSellingStatus()).isEqualTo(ProductSellingStatus.SELLING);
        assertThat(request.getPrice()).isEqualTo(4800);
    }
}