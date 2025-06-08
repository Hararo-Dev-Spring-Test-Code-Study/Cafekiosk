package sample.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    // Order 리스트를 반환하도록
    List<OrderProduct> findAllByOrder(Order order);
}
