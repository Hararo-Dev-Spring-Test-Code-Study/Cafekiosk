package sample.cafekiosk.spring.domain.order;

import lombok.*;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id") // 외래키 이름 지정, order_id 라는 컬럼으로 저장되도록
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
