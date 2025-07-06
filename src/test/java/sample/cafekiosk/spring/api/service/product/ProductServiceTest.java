package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
@Transactional
class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  @DisplayName("신규 상품을 생성하면 이전 번호 기준 다음 번호가 자동 부여된다.")
  @Transactional
  void createProduct() {
    // given
    int beforeNumber = productRepository.findAll().size();
    String nextProductNumber = String.format("%03d", beforeNumber + 1);

    ProductCreateRequest request = new ProductCreateRequest(
        ProductType.HANDMADE,
        ProductSellingStatus.SELLING,
        "죠리퐁라떼",
        5500
    );

    // when
    ProductResponse response = productService.createProduct(request);

    // then
    assertThat(response.getProductNumber()).isEqualTo(nextProductNumber);
    assertThat(response.getName()).isEqualTo("죠리퐁라떼");
    assertThat(response.getPrice()).isEqualTo(5500);
  }


  @Test
  @DisplayName("상품이 하나도 없을 경우 첫 상품 번호는 001이 된다")
  void createProduct_noProduct_shouldStartWith001() {
    // given
    productRepository.deleteAll();

    ProductCreateRequest request = new ProductCreateRequest(
        ProductType.BAKERY,
        ProductSellingStatus.SELLING,
        "크루아상",
        3000
    );

    ProductResponse response = productService.createProduct(request);

    assertThat(response.getProductNumber()).isEqualTo("001");
  }
}