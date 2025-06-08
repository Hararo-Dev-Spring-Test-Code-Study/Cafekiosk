package sample.cafekiosk.spring.domain.orderproduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {

    // 이 엔티티의 역할은 두 엔티티를 이어주는 역할

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성전략
    private Long id;

    // fetch 전략을 Lazy로 선언해주는 것이 좋다
    // 왜?
    // 기본적으로 Lazy하게 로딩해서 내가 필요한 부분, 즉시 사용해야하는 부분만 로딩해서 사용하는 것이 좋다
    // 주문 입장에서는 orderProduct에 대해 알아야하기 때문에 양방향 설정
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    // 상품 자체는 자신이 어떤 주문에 담기는지 알 필요가 없으니 요정도로 설정하고
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

}