package sample.cafekiosk.spring.domain.order;

// Lombok 라이브러리에서 제공하는 AccessLevel 열거형(enum)
// Lombok 어노테이션에서 생성자나 메서드의 접근 제어자(접근 수준) 지정시 사용
// 예시: @NoArgsConstructor(access = AccessLevel.PROTECTED)
// 기본 생성자의 접근 수준을 protected로 설정하겠다는 의미
import lombok.AccessLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

// JPA에서 제공하는 Entity 매핑 관련 어노테이션과 클래스
// @Entity, @Id, @Table, @GeneratedValue, @Enumerated, @OneToMany 등
import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
// Java의 Stream API 사용위해 필요한 클래스
// 컬렉션(Collection)을 스트림(Stream)으로 가공한 후
// 다시 원하는 형태로 수집(collect)할 수 있또록 도와주는 유틸리티 메서드들을 제공
// Stream이란 데이터의 흐름을 추상화한 개념으로
// 컬렉션 데이터를 선언형(무엇으로 할 것인지) 방식으로 처리하는 Java 기능
// 1. 생성 .stream()으로 스트림 객체 생성
// 2. 중간 연산: .filter(), .map(), .sorted() 등 데이터를 가공
// 3. 최종 연산: .collect(), .forEach(), .count() 등 결과 반환
import java.util.stream.Collectors;

@Getter
// 기본 생성자의 접근 수준을 PROTECTED로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 매핑되는 테이블 이름을 명시(SQL 예약어 order 피하기 위해 복수형 사용)
@Table(name = "orders")
// JPA가 관리하는 엔티티 클래스임을 선언
@Entity
public class Order extends BaseEntity {

    // 기본 키
    @Id
    // 자동 증가 전략 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 상태를 나타내는 열거형(OrderStatus)
    // 주문 상태 EnumType을 DB에 "INIT" 같은 문자열로 저장
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 주문 총액
    private int totalPrice;

    // 주문 등록 시간
    private LocalDateTime registeredDateTime;

    // OrderProduct와의 1:N 관계 설정
    // OrderProduct에 있는 order 필드가 주인
    // cascade : Order 저장 시 연관된 OrderProduct도 함께 저장됨
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    // 주문 생성 시 사용되는 생성자
    public Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        // 상품 리스트를 OrderProduct 리스트로 변환해서 저장
        // -> 중간 테이블 역할을 하는 OrderProduct를 활용한 객체 간 연관 관계 매핑
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    // new 키워드 대신 Order.create(...) 방식으로 생성할 수 있게 도와주는 정적 팩토리 메서드
    // 의미 전달이 명확해지고 테스트 코드 등에서 가독성 향상
    // 내부적으로 Order 클래스의 생성자 호출
    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return new Order(products, registeredDateTime);
    }

    // 상품 리스트의 가격 합계를 계산해 반환
    // stream().mapToInt().sum()을 이용한 간결한 구현
    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

}
