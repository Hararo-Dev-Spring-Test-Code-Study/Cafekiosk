package sample.cafekiosk.spring.api.controller.product.request;

import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// 신규 상품 등록을 위한 입력값을 담는 요청 전용 객체
public record ProductCreateRequest(
    // 요구사항 중 “파라미터에 대한 최소한의 검증을 수행”
    @NotBlank(message = "상품 번호는 비어 있을 수 없습니다.")
    String productNumber,
    @NotNull(message = "상품 타입은 필수 항목입니다.")
    ProductType type,
    @NotNull(message = "판매 상태는 필수 항목입니다.")
    ProductSellingStatus sellingStatus,
    @NotBlank(message = "상품 이름은 비어 있을 수 없습니다.")
    String name,
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    int price
) {}