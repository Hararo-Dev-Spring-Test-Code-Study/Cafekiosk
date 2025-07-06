package sample.cafekiosk.spring.api.controller.product.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

  // 요구사항 중 “파라미터에 대한 최소한의 검증을 수행”
  @NotNull(message = "상품 타입은 필수 항목입니다.")
  private ProductType type;

  @NotNull(message = "판매 상태는 필수 항목입니다.")
  private ProductSellingStatus sellingStatus;

  @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
  private String name;

  @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
  private int price;

  @Builder
  public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name,
      int price) {
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }
}