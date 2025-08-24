package sample.cafekiosk.test.fixture;

import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public final class ProductFixtures {
  private ProductFixtures() {}

  // 판매 상태만을 인자로 받고 나머지 변수들은 기본값으로 고정
  public static Product withStatus(ProductSellingStatus status) {
    return Product.builder()
        .productNumber("001")
        .type(ProductType.HANDMADE)
        .sellingStatus(status)
        .name("테스트상품")
        .price(1000)
        .build();
  }

  // 총액 검증 전용: 상품번호/가격만 제어, 나머지는 기본값
  public static Product priced(String number, int price) {
    return Product.builder()
        .productNumber(number)
        .type(ProductType.HANDMADE)
        .sellingStatus(SELLING)
        .name("테스트메뉴")
        .price(price)
        .build();
  }
}