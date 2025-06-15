package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.CafekioskApplication;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(classes = CafekioskApplication.class)
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockRepository stockRepository;

    @DisplayName("고객이 선택한 상품 번호 목록으로 주문을 생성하면, 주문 상태는 INIT이고 총 금액이 포함된다")
    @Test
    void createOrder() {
        // given
        Product americano = createProduct("001", "아메리카노", 4000, ProductType.BOTTLE);
        Product latte = createProduct("002", "라떼", 4500, ProductType.BOTTLE);
        productRepository.saveAll(List.of(americano, latte));

        List<String> productNumbers = List.of("001", "002");

        // when
        Order order = orderService.createOrder(productNumbers);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
        assertThat(order.getRegisteredDateTime()).isNotNull();
        assertThat(order.getTotalPrice()).isEqualTo(americano.getPrice() + latte.getPrice());
    }

    private Product createProduct(String productNumber, String name, int price, ProductType type) {
        return new Product(productNumber, name, price, type, ProductSellingStatus.SELLING);
    }

    @Test
    void createOrder_shouldFail_whenStockIsNotEnough() {
        // given
        Product bottle = createProduct("001", "콜라", 1500, ProductType.BOTTLE);
        stockRepository.save(createStock("001", 1));
        productRepository.save(bottle);

        // when
        List<String> orderRequest = List.of("001", "001"); // 2개 주문, 재고는 1개

        // then
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 포함되어 있습니다.");
    }

    private Stock createStock(String productNumber, int quantity) {
        return new Stock(productNumber, quantity);
    }
}
