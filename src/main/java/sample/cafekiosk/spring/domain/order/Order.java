package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태

    private LocalDateTime orderDateTime;    // 주문 등록 시간

    private int totalPrice; // 총 금액

    // 주문된 상품들. 1:N 관계
    @OneToMany(mappedBy = "order")
    private List<Product> products = new ArrayList<>();

    @Builder
    private Order(OrderStatus status, LocalDateTime orderDateTime, int price, List<Product> products) {
        this.status = status;
        this.orderDateTime = orderDateTime;
        this.totalPrice = price;
        this.products = products;
    }

    public static Order create(List<Product> products, LocalDateTime orderDateTime) {
        Order order = Order.builder()
                .status(OrderStatus.ORDER)
                .price(calculateTotalPrice(products))
                .orderDateTime(orderDateTime)
                .products(products)
                .build();

        return order;
    }

    // 총 주문 금액 계산
    private static int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
}
