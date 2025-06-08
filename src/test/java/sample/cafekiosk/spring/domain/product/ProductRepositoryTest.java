package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test") // application-test.yml 을 사용하기 위해
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("판매 상태가 '판매중' 또는 '판매보류' 인 상품만 조회된다")
    @Test
    void findAllBySellingStatusIn() {
        // given - 테스트용 상품 데이터 저장
        Product sellingProduct = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000).build();

        Product holdProduct = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("카페라떼")
                .price(4500).build();

        Product stoppedProduct = Product.builder()
                .productNumber("003")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.STOP_SELLING)
                .name("크루아상")
                .price(3500).build();


        productRepository.saveAll(List.of(sellingProduct, holdProduct, stoppedProduct));

        // when - forDisplay 를 통해 판매중 또는 보류 상태인 상품만 조회
        List<Product> result = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());


        // then - 실제로 2 개인지, sellingStatus 가 SELLING, HOLD 로 순서대로 들어가 있는지
        assertThat(result).hasSize(2)
                .extracting("sellingStatus")
                .containsExactly(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD);

    }

    @DisplayName("상품 번호 리스트에 해당하는 상품들이 조회된다")
    @Test
    void findAllByProductNumberIn() {
        // given - 세 개의 상품을 저장
        Product product1 = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(ProductType.BAKERY)
                .sellingStatus(ProductSellingStatus.STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when - productNumber 리스트로 상품 조회
        List<String> productNumbers = List.of("001", "003");
        List<Product> result = productRepository.findAllByProductNumberIn(productNumbers);

        // then - 해당 번호에 일치하는 상품 2개가 반환되는지 검증
        assertThat(result).hasSize(2)
                .extracting("productNumber")
                .containsExactly("001", "003");
    }

}