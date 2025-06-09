package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("여러 상품으로 주문을 생성하면, 총 금액은 각 상품 가격의 합이 된다")
    @Test
    void calculateTotalPrice() {
        // given
        Product americano = createProduct("001", "아메리카노", 4000);
        Product latte = createProduct("002", "라떼", 4500);
        List<Product> products = List.of(americano, latte);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(americano.getPrice() + latte.getPrice());
    }

    private Product createProduct(String productNumber, String name, int price) {
        return new Product(productNumber, name, price, ProductSellingStatus.SELLING);
    }
}