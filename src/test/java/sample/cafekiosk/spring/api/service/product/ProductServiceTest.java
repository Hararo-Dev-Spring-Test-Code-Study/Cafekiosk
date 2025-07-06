package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.controller.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 실행 후 롤백
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("신규 상품 등록 테스트")
    void createProduct_success() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                "딸기라떼",
                ProductType.HANDMADE,
                ProductSellingStatus.SELLING,
                5200
        );

        // when
        ProductResponse response = productService.createProduct(request);

        // then
        Optional<Product> savedProduct = productRepository.findTopByOrderByProductNumberDesc();
        assertThat(savedProduct).isPresent();
        assertThat(response.getProductNumber()).isEqualTo(savedProduct.get().getProductNumber());
        assertThat(response.getName()).isEqualTo("딸기라떼");
        assertThat(response.getPrice()).isEqualTo(5200);
    }

    @Test
    @DisplayName("상품번호 자동 증가 테스트")
    void generateNextProductNumber_autoIncrement() {
        // given - 기존에 상품 1개 저장 (productNumber: "009")
        Product product = Product.builder()
                .productNumber("009")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("바닐라라떼")
                .price(4700)
                .build();
        productRepository.save(product);

        // when - 다음 상품 등록
        ProductCreateRequest request = new ProductCreateRequest(
                "콜드브루",
                ProductType.HANDMADE,
                ProductSellingStatus.SELLING,
                4800
        );
        ProductResponse response = productService.createProduct(request);

        // then - productNumber가 "010"으로 자동 증가되어야 함
        assertThat(response.getProductNumber()).isEqualTo("010");
    }
}