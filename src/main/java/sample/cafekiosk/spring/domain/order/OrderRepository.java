package sample.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'COMPLETED' AND DATE(o.registeredDateTime) = :targetDate")
    List<Order> findCompletedOrdersByDate(@Param("targetDate") LocalDate targetDate);
}