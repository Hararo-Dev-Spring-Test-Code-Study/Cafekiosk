package sample.cafekiosk.spring.domain.order;

import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    //Green 단계 통과를 위해 Order 클래스의 최소한의 구현을 함
    //현재 Order 객체는 주문 생성 시 필요한 정보만을 포함함
    private final List<String> productNumbers; //주문에 포함된 상품 번호 리스트
    private final LocalDateTime registeredDateTime; //주문이 등록된 시간
    private final List<Product> products; //주문에 포함된 상품 리스트
    
    private Order(List<String> productNumbers, LocalDateTime registeredDateTime) {
        this.productNumbers = productNumbers;
        this.registeredDateTime = registeredDateTime;
        this.products = null;
    } //생성자는 외부에서 직접 호출 못하도록 private 접근자 사용, 객체 생성을 create() 메서드를 통해서만 하도록 유도
    
    private Order(List<Product> products, LocalDateTime registeredDateTime, boolean isProductList) {
        this.products = products;
        this.registeredDateTime = registeredDateTime;
        this.productNumbers = null;
    }
    
    public static Order create(List<String> productNumbers, LocalDateTime registeredDateTime) {
        return new Order(productNumbers, registeredDateTime);
    } //정적 팩토리 메서드 패턴 사용, 추후 로직 추가(validate, init등)가 쉽도록
    
    public static Order createWithProducts(List<Product> products, LocalDateTime registeredDateTime) {
        return new Order(products, registeredDateTime, true);
    }
    
    public List<String> getProductNumbers() {
        return productNumbers;
    } //테스트 코드에서 필드 값을 검증할 수 있도록 getter 메서드 제공
    
    public LocalDateTime getRegisteredDateTime() {
        return registeredDateTime;
    }
    
    public int getTotalAmount() {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
} 