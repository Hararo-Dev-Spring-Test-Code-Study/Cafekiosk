package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    // Auditing이 되도록 BaseEntity 확장해준다!
    // order라는 것이 SQL문에서 예약어이기 때문에 테이블명으로 order가 불가능하다.
    // -> @Table 어노테이션을 통해 사용할 테이블의 이름을 지정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성전략
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    // cascade로 생명주기를 걸 수 있는데 일단은 ALL로 걸어두었습니다.
    // OrderProduct는 order가 생성, 수정, 삭제될 때 항상 변경이 같이 일어나도록 ALL로
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

}
