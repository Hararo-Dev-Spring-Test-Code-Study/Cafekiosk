// 이 클래스는 주문(Order)과 상품(Product) 사이의 다대다(N:M) 관계를 표현하는 연결(중간) 엔티티

package sample.cafekiosk.spring.domain.orderproduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // OrderProduct는 하나의 Order에 속할 수 있음
    // N:1 관계로 하나의 주문(Order)에 여러 OrderProduct가 연결될 수 있음
    // 지연 로딩(lazy loading): 실제 사용할 때까지 DB 조회를 미룸 : 성능 최적화를 위해 사용됨
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    // OrderProduct는 하나의 Product에 연결됨
    // 하나의 상품은 여러 주문 상품으로 참조될 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    // 생성자를 통해 명시적으로 연결
    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

}

// Order와 Product의 다대다 관계를 풀어낸 이유
// JPA는 다대다(N:M) 관계를 직접 매핑하는 것을 지양함
// 대신에 중간 엔티티(OrderProduct)를 두고 각각을 ManyToOne으로 풀어냄
// 중간 테이블에 추가 필드(예:수량, 가격 등)를 넣을 수 있고 유지 보수도 쉬워짐

//// 주문 생성 시 사용되는 생성자
//public Order(List<Product> products, LocalDateTime registeredDateTime) {
//    this.orderStatus = OrderStatus.INIT;
//    this.totalPrice = calculateTotalPrice(products);
//    this.registeredDateTime = registeredDateTime;
//    // 상품 리스트를 OrderProduct 리스트로 변환해서 저장
//    // -> 중간 테이블 역할을 하는 OrderProduct를 활용한 객체 간 연관 관계 매핑
//    this.orderProducts = products.stream()
//            .map(product -> new OrderProduct(this, product))
//            .collect(Collectors.toList());

// Order 클래스의 생성자의 new OrderProduct(this, product) 에서 생성
// Products 리스트를 stream으로 변환하고 각 Product마다 OrderProduct(this, product) 생성
// OrderProduct 객체들을 리스트로 모아 orderProducts 필드에 저장