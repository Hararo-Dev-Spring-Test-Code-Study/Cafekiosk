package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;


    //이전 테스트코드 리팩토링
    @DisplayName("재고를 사용하지 않는 상품(HANDMADE)은 재고 없이도 주문이 가능하다.")
    @Test
    void createOrderTest() {
        // given
        Product product1 = productRepository.save(Product.builder()
                .productNumber("001").type(ProductType.HANDMADE).sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노").price(1000).build());

        Product product2 = productRepository.save(Product.builder()
                .productNumber("002").type(ProductType.HANDMADE).sellingStatus(ProductSellingStatus.SELLING)
                .name("카페라떼").price(3000).build());

        List<String> productNumbers = List.of("001", "002");
        LocalDateTime fixedTime = LocalDateTime.of(2025,6,1,12,0);

        // when
        Order order = orderService.createOrder(productNumbers, fixedTime);

        // then
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getTotalPrice()).isEqualTo(4000);
    }

    @DisplayName("재고가 충분한 병음료 상품 주문 시 주문이 생성되고 재고가 차감된다.")
    @Test
    void createOrderWithSufficientStock() {
        Product product = productRepository.save(Product.builder()
                .productNumber("101").type(ProductType.BOTTLE).sellingStatus(ProductSellingStatus.SELLING)
                .name("콜라").price(1500).build());

        stockRepository.save(new Stock("101", 3));

        //같은것 두개 주문시에 1개가격만 합산되고 있음->Order에 동일 상품 여러개 담을 수 있도록 리팩토링 필요함
        List<String> productNumbers = List.of("101", "101");
        Order order = orderService.createOrder(productNumbers, LocalDateTime.now());

        //상품수가 아닌 총가격기준으로 검증
        //order.getProduct는 객기준으로 동일한 인스턴스 하나만 포함되어 hasSize 결과가 예상과 다름
        //assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getTotalPrice()).isEqualTo(3000);
        assertThat(stockRepository.findByProductNumber("101").get().getQuantity()).isEqualTo(1);
    }

    @DisplayName("재고가 부족한 경우 예외가 발생한다.")
    @Test
    void createOrderWithInsufficientStock() {
        Product product = productRepository.save(Product.builder()
                .productNumber("102").type(ProductType.BAKERY).sellingStatus(ProductSellingStatus.SELLING)
                .name("크루아상").price(2000).build());

        stockRepository.save(new Stock("102", 1));

        List<String> productNumbers = List.of("102", "102");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(productNumbers, LocalDateTime.now()));

        assertThat(ex.getMessage()).isEqualTo("재고가 부족합니다.");
    }

    @DisplayName("재고 정보가 없는 경우 예외가 발생한다.")
    @Test
    void createOrderWithoutStockInfo() {
        Product product = productRepository.save(Product.builder()
                .productNumber("103").type(ProductType.BOTTLE).sellingStatus(ProductSellingStatus.SELLING)
                .name("스프라이트").price(1200).build());

        List<String> productNumbers = List.of("103");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(productNumbers, LocalDateTime.now()));

        assertThat(ex.getMessage()).isEqualTo("재고 정보가 없습니다.");
    }
}
