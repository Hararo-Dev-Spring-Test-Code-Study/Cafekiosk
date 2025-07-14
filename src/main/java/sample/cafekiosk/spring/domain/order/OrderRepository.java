package sample.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRegisteredDateTimeBetweenAndOrderStatus(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            OrderStatus status
    );
}
