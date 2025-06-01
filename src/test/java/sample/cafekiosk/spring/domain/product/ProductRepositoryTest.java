package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("판매상태가 SELLING인 상품을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        List<ProductSellingStatus> statuses = List.of(ProductSellingStatus.SELLING);

        // when
        List<Product> result = productRepository.findAllBySellingStatusIn(statuses);

        // then
        assertThat(result).hasSize(1)
                .extracting("name")
                .containsExactly("아메리카노");
    }
}