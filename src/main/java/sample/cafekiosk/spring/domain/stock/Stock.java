package sample.cafekiosk.spring.domain.stock;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    public Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public boolean isQuantityLessThan(long requiredQuantity) {
        return this.quantity < requiredQuantity;
    }

    public void deductQuantity(long quantityToDeduct) {
        if (isQuantityLessThan(quantityToDeduct)) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity -= quantityToDeduct;
    }
}
