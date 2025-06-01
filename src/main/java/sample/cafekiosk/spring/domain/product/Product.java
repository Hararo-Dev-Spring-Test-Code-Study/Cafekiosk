package sample.cafekiosk.spring.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.*;

@Getter // 클래스 내의 모든 필드들에 getter 메서드 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 만들어줌. JPA 에서는 파라미터가 없는 기본 생성자를 만드는 것이 필수적이라고 함.
@Entity // JPA 엔티티 클래스임. JPA 가 이 클래스를 DB 테이블로 매핑
public class Product extends BaseEntity {
    @Id // 이 속성이 기본 키임.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 이 키 값을 자동으로 생성. auto increment 방식
    private Long id;

    private String productNumber;

    // String 타입으로 EnumType 을 저장하겠다는 의미 / ORDINAL : 순서 값을 저장하겠음 ex) HANDMADE : 0, BOTTLE : 1 ..
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    // 추가도 테스트 해보고 싶어서 생성자 추가
    // @Setter 를 통해서 테스트 할 수도 있음
    public Product(String productNumber, ProductType type, ProductSellingStatus status, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = status;
        this.name = name;
        this.price = price;
    }

}
