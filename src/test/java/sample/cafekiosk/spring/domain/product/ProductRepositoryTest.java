package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

}





//package sample.cafekiosk.spring.domain.product;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.tuple;
//
//import java.util.List;
//import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
//
//// @SpringBootTest
//@DataJpaTest
//class ProductRepositoryTest {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    // @BeforeEach : data.sql : SELLING 1, HOLD 1, STOP_SELLING 1
//
//    @DisplayName("판매중인 상품들을 조회한다.")
//    @Test
//    void findAllBySellingStatusIn_withSelling() {
//        // given
//
//        // when
//        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING));
//
//        // then
//        assertThat(products).hasSize(1)
//                .extracting("productNumber", "name", "sellingStatus")
//                .containsExactlyInAnyOrder(
//                        tuple("001", "아메리카노", SELLING)
//                );
//    }
//    @DisplayName("판매중이거나 판매보류중인 상품들을 조회한다.")
//    @Test
//    void findAllBySellingStatusIn_withSellingAndHold() {
//        // given
//
//        // when
//        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));
//
//        // then
//        assertThat(products).hasSize(2)
//                .extracting("productNumber", "name", "sellingStatus")
//                .containsExactlyInAnyOrder(
//                        tuple("001", "아메리카노", SELLING),
//                        tuple("002", "카페라떼", HOLD)
//                );
//    }
//    @DisplayName("판매중지 상품들을 조회한다.")
//    @Test
//    void findAllBySellingStatusIn_withStopSelling() {
//        // given
//
//        // when
//        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(STOP_SELLING));
//
//        // then
//        assertThat(products).hasSize(1)
//                .extracting("productNumber", "name", "sellingStatus")
//                .containsExactlyInAnyOrder(
//                        tuple("003", "크루아상", STOP_SELLING)
//                );
//    }
//}
//
