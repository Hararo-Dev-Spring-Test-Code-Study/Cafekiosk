// week 8 생성
// Request DTO : 클라이언트로부터 요청 데이터를 받을 때 사용하는 객체
// @RequestBody로 사용됨 : 사용자 -> 서버로 오는 데이터를 담음
// Entity 와는 분리되어 있어야 함(보안/확장성 이유)

package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductCreateRequest {

    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    // @Builder를 사용해 객체 생성시 가독성 좋은 빌더 패턴 제공
    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    // 서비스 계층에서 이 DTO(request)를 JPA 엔티티 Product 객체로 변환하는 메서드
    // nextProductNumber는 서비스에서 생성하는 고유 상품 번호를 전달받아 설정
    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}
