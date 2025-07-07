package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@SpringBootTest
@Transactional
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // DB 비우고 시작
    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("신규 상품을 등록하면, 상품 번호가 자동 생성되고 응답 객체로 반환된다.")
    void createProduct() {
        // given
        productRepository.save(createProduct("001", "아이스티", 1000));
        productRepository.save(createProduct("002", "아메리카노", 2000));

        ProductCreateRequest request = new ProductCreateRequest(
                HANDMADE,
                HOLD,
                "카페라떼",
                4500
        );

        // when
        ApiResponse<ProductResponse> response = productService.createProduct(request);

        // then
        assertThat(response.getStatus()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("새로운 상품이 등록되었습니다.");
        assertThat(response.getData().getName()).isEqualTo("카페라떼");
        assertThat(response.getData().getProductNumber()).isEqualTo("003");
    }

    @Test
    @DisplayName("등록된 상품이 없으면 신규 상품 번호는 001로 등록된다.")
    void createFirstProduct() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                BOTTLE,
                HOLD,
                "오렌지 쥬스",
                2000
        );

        // when
        ApiResponse<ProductResponse> response = productService.createProduct(request);

        // then
        assertThat(response.getData().getProductNumber()).isEqualTo("001");
        assertThat(response.getData().getType()).isEqualTo(BOTTLE);
        assertThat(response.getData().getName()).isEqualTo("오렌지 쥬스");
        assertThat(response.getData().getPrice()).isEqualTo(2000);
    }

    // 테스트용 상품 생성 메서드
    private Product createProduct(String number, String name, int price) {
        return Product.builder()
                .productNumber(number)
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name(name)
                .price(price)
                .build();
    }
}