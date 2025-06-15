// Week 7 추가

package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

// 이 클래스가 데이터베이스 테이블과 매핑됨을 나타냄
// @Entity가 선언된 클래스는 DB 테이블 하나에 대응(Stock 테이블 생성/관리)
import javax.persistence.Entity;
// Auto Increment
import javax.persistence.GeneratedValue;
// 열거형(enum)을 통해 ID 생성 전략 설정
// AUTO : JPA가 사용중인 DB에 따라 자동 선택(가장 유연)
// IDENTITY : DB의 AUTO_INCREMENT 기능 사용(MYSQL 등에서 사용)
// SEQUENCE : DB의 시퀀스(Sequence) 객체 사용(Oracle 등에서 사용)
// TABLE : 별도의 테이블을 만들어 ID 생성값 관리
import javax.persistence.GenerationType;
// PK 지정
import javax.persistence.Id;

@Getter
// 접근제어자 protected로 제한해 의도된 방식(정적 팩토리 메서드나 builder)를 통해서만 생성되도록 유도
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 이 클래스는 JPA가 관리하는 엔티티 클래스이며 DB 테이블로 매핑됨
@Entity
public class Stock extends BaseEntity {

    // 해당 필드(id)를 기본 키(PK)로 지정
    @Id
    // DB의 Auto Increment 전략 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 구별 위한 고유 번호
    private String productNumber;

    // 남아있는 재고 수량
    private int quantity;

    // Lombok의 빌더 패턴 적용 해객체 생성 유연성 높임
    // private으로 생성자 만들어 빌더 패턴이나 팩토리 메서드 사용하도록 유도
    @Builder
    private Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    // 정적 팩토리 메서드
    // 객체 생성 시 new 대신 create() 사용 -> 가독성 및 유연성 증가
    public static Stock create(String productNumber, int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    // 수량 비교 메서드
    // 현재 재고인 this.quantity 차감 전에 사용할 수 있는 검증 메서드
    // 현재 재고 수량 < 인자값 (재고부족상황)이면 true
    public boolean isQuantityLessThan(int quantity) {
        return this.quantity < quantity;
    }

    // 재고 차감 메서드
    public void deductQuantity(int quantity) {
        // 수량 비교 메서드로 검증 후 재고부족하면 예외 발생시킴
        if (isQuantityLessThan(quantity)) {
            throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
        }
        // 재고가 있다면 현재 재고 - 1
        this.quantity -= quantity;
    }

}
