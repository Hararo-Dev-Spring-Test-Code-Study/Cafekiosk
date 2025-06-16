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

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(classes = CafekioskApplication.class)
@Transactional
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

        stockRepository.saveAll(List.of(
                createStock("001", 2),
                createStock("002", 2)
        )); // 재고 저장

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

    @DisplayName("고객이 선택한 상품의 재고가 충분한 경우, 정상적으로 주문이 생성된다")
    @Test
    void createOrder_success_whenStockIsSufficient() {
        // given
        Product bottle = createProduct("001", "콜라", 1500, ProductType.BOTTLE);
        Product bakery = createProduct("002", "크루아상", 2000, ProductType.BAKERY);
        productRepository.saveAll(List.of(bottle, bakery));

        stockRepository.saveAll(List.of(
                createStock("001", 2), // 병음료 재고 2개
                createStock("002", 3)  // 베이커리 재고 3개
        ));

        List<String> orderRequest = List.of("001", "002"); // 병음료 1개, 베이커리 1개 주문

        // when
        Order order = orderService.createOrder(orderRequest);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(
                bottle.getPrice() + bakery.getPrice()
        );
        assertThat(order.getOrderProducts()).hasSize(2); // 총 2개 주문
    }

    @DisplayName("고객이 선택한 상품 수량이 재고보다 많으면, 주문 생성 시 예외가 발생한다")
    @Test
    void createOrder_fail_whenStockIsInsufficient() {
        // given
        Product bottle = createProduct("001", "콜라", 1500, ProductType.BOTTLE);
        productRepository.save(bottle);
        stockRepository.save(createStock("001", 1)); // 재고 1개

        List<String> orderRequest = List.of("001", "001"); // 2개 주문

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 포함되어 있습니다.");
    }

    @DisplayName("고객이 선택한 상품에 재고 정보가 없으면, 주문 생성 시 예외가 발생한다")
    @Test
    void createOrder_shouldFail_whenStockIsNotEnough() {
        // given
        Product bottle = createProduct("001", "콜라", 1500, ProductType.BOTTLE);
        productRepository.save(bottle);
        // stockRepository에 재고 저장 안 함 → 재고 없음

        List<String> orderRequest = List.of("001");

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고 정보 없음");
    }

    private Stock createStock(String productNumber, int quantity) {
        return new Stock(productNumber, quantity);
    }
}
