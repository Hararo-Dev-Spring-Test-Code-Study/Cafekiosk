
package sample.cafekiosk.spring.domain.product;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("판매 상태가 SELLING 또는 HOLD인 상품만 조회된다")
    @Test
    void findAllBySellingStatusIn() {
        // given
        //이미 h2 데이터베이스가 있으므로 데이터 처리안함
        //H2 임베디드 DB가 자동으로 뜨면서 테스트 시작 시점에 product 테이블에 데이터가 이미 들어가 있는 상태

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(
                List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD)
        );

        // then
        //총2개, produdctNumber확인
        assertThat(products).hasSize(2)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "002");
    }
}