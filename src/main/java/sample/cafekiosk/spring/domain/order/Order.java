package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

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

}
