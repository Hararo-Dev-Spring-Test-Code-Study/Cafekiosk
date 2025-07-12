package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor // Post 요청이 올때 objectMapper가 역직렬화를 도와주는데, 이때 기본 생성자를 쓰기 때문에
public class ProductCreateRequest {

    // validation 라이브러리를 통해 파라미터의 유효성 검증
    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;

    @NotBlank(message = "상품 이름은 필수입니다.") // Null empty 둘다 통과 안함
    // 스트링 검증
    // @NotNull Null이 아니어야함 ,빈문자열 공백이 있는 문자열은 통과
    // @NotEmpty 공백은 통과, 빈문자열만 걸림
    // 상품 이름에 대한 길이 제한이 잇다고 가정했을 때 여기서 @Max(20)과 같이 검증할 수 있다
    // 하지만 여기서 이러한 부분을 검증하는 것이 맞는지 고민해볼 필요가 있다. -> Controller에서 가져야할 책임인가?
    private String name;

    @Positive(message = "상품 가격은 0 이상입니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
