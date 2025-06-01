package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static sample.cafekiosk.spring.domain.product.ProductType.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;

// @SpringBootTest

// 테스트 전용 프로필 적용 (data.sql 미실행)
@ActiveProfiles("test") // -> application-test.yml 기준으로 실행함
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

//    @BeforeEach
//    void setUp() {
//        productRepository.deleteAll();
//    }

    @DisplayName("판매 가능한 상태(SELLING, HOLD)의 상품을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        // 상품 생성
        Product p1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product p2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        Product p3 = Product.builder()
                .productNumber("003")
                .type(BAKERY)
                .sellingStatus(STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

        productRepository.saveAll(List.of(p1, p2, p3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                        tuple("002", HANDMADE, HOLD, "카페라떼", 4500)
                );
    }
}