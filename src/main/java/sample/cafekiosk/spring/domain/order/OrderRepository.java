package sample.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.spring.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
