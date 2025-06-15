package sample.cafekiosk.spring.domain.stock;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
// 그 상품이 몇 개가 있는지
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, unique = true)
    private String productNumber;

    public void reduceQuantity(Integer quantity) {
        if(this.quantity - quantity < 0) {
            this.quantity = 0;
        }
        this.quantity -= quantity;
    }
}
