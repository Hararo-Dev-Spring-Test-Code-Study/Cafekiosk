package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    @DisplayName("상품 번호 리스트를 받아 주문을 생성할 수 있다")
    void createOrderWithProductNumbers() {
        // Order 클래스에 productNumbers 필드, getProductNumbers()메서드, create()메서드가 없음 -> Red 상태
        // given
        List<String> productNumbers = List.of("001", "002", "003");
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(productNumbers, registeredDateTime);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getProductNumbers()).hasSize(3)
                .containsExactly("001", "002", "003");
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }
} 