package sample.cafekiosk.spring.domain.order;

import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime registeredDateTime;

    @ManyToMany
    private List<Product> products;

    public static Order create(List<Product> products, LocalDateTime time) {
        Order order = new Order();
        order.orderStatus = OrderStatus.INIT;
        order.registeredDateTime = time;
        order.products = products;
        return order;
    }

    public int getTotalPrice() {
        return products.stream().mapToInt(Product::getPrice).sum();
    }
}

