package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("판매 상태가 SELLING 또는 HOLD인 상품만 조회된다")
    @Test
    void findAllBySellingStatusIn() {
        // given
//        Product p1 = createProduct("001", "아메리카노", ProductSellingStatus.SELLING);
//        Product p2 = createProduct("002", "카페라떼", ProductSellingStatus.HOLD);
//        Product p3 = createProduct("003", "크루아상", ProductSellingStatus.STOP_SELLING);
//
//        productRepository.saveAll(List.of(p1, p2, p3));

        // when
        List<Product> result = productRepository.findAllBySellingStatusIn(
                List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD)
        );

        // then
        assertThat(result).hasSize(2)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "002");
    }

    private Product createProduct(String number, String name, ProductSellingStatus status) {
        return Product.builder()
                .productNumber(number)
                .type(ProductType.HANDMADE)
                .sellingStatus(status)
                .name(name)
                .price(4000)
                .build();
    }
}
