package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {
    @Autowired //
    private ProductRepository productRepository;

    @DisplayName("판매중 또는 판매보류 상태의 상품만 조회된다")
    @Test
    void findAllBySellingStatusIn() {

        // when - forDisplay() 를 통해 SELLING, HOLD 만 필터링
        // findAllBySellingStatusIn 의 sellingTypes 에 [SELLING, HOLD] 전달
        // data.sql 을 통해 타입이 selling_status 가 SELLING 과 HOLD 인 데이터가 하나씩 들어가 있음.
        List<Product> result = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        List<Product> gun = productRepository.findAll();
        for(Product p : gun){
            System.out.println(p.getCreatedDateTime());
        }

        // then
        // 2 개인지 확인하고,
        // sellingStatus 를 추출해서 SELLING 이고, HOLD 인지 확인
        assertThat(result).hasSize(2)
            .extracting("sellingStatus")
            .containsExactlyInAnyOrder(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD);
    }

//    @DisplayName("판매중 또는 판매보류 상태의 상품을 저장하고 조회하면, 해당 상품만 반환된다")
//    @Test
//    void saveAndFindProductsBySellingStatus() {
//        // 데이터를 추가하는 등의 로직을 검증하려면 아래 deleteAll 을 통해 초기화 후 사용
//         productRepository.deleteAll();
//        // given
//        Product product1 = new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
//        Product product2 = new Product("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
//        Product product3 = new Product("003", ProductType.BAKERY, ProductSellingStatus.STOP_SELLING, "크루아상", 3500);
//
//        productRepository.saveAll(List.of(product1, product2, product3));
//
//        // when
//        List<Product> result = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
//
//        // then
//        assertThat(result).hasSize(2)
//            .extracting("sellingStatus")
//            .containsExactlyInAnyOrder(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD);
//    }

}
