package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    @DisplayName("상품 리스트를 받아 주문을 생성할 수 있다")
    void createOrderWithProducts() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000),
                createProduct("003", 3000)
        );
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getProductNumbers()).hasSize(3)
                .containsExactly("001", "002", "003");
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    @Test
    @DisplayName("주문의 총 금액을 계산할 수 있다")
    void calculateTotalAmount() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000),
                createProduct("003", 3000)
        );
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getTotalAmount()).isEqualTo(6000);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(price)
                .name("메뉴 이름")
                .build();
    }
} 