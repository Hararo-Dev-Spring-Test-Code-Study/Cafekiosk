//package sample.cafekiosk.spring.domain.product;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("test")
//@DataJpaTest
//class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Test
//    @DisplayName("특정 판매 상태를 가진 상품만 조회된다")
//    void findAllBySellingStatusIn() {
//        // given
//        Product americano = createProduct("001", "아메리카노", 4000, ProductSellingStatus.SELLING);
//        Product latte = createProduct("002", "카페라떼", 4500, ProductSellingStatus.HOLD);
//        Product croissant = createProduct("003", "크루아상", 3000, ProductSellingStatus.STOP_SELLING);
//        productRepository.saveAll(List.of(americano, latte, croissant));
//
//        // when
//        List<Product> result = productRepository.findAllBySellingStatusIn(
//                List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD)
//        );
//
//        // then
//        assertThat(result).hasSize(2)
//                .extracting("productNumber")
//                .containsExactlyInAnyOrder("001", "002");
//    }
//
//    @Test
//    @DisplayName("상품 번호 리스트로 등록된 상품들을 조회할 수 있다")
//    void findAllByProductNumberIn() {
//        // given
//        Product americano = createProduct("001", "아메리카노", 4000, ProductSellingStatus.SELLING);
//        Product latte = createProduct("002", "카페라떼", 4500, ProductSellingStatus.SELLING);
//        productRepository.saveAll(List.of(americano, latte));
//
//        // when
//        List<Product> result = productRepository.findAllByProductNumberIn(List.of("001", "002"));
//
//        // then
//        assertThat(result).hasSize(2)
//                .extracting("productNumber")
//                .containsExactlyInAnyOrder("001", "002");
//    }
//
//    private Product createProduct(String number, String name, int price, ProductSellingStatus status) {
//        return new Product(number, name, price, status);
//    }
//}