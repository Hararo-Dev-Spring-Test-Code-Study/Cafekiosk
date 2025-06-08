package sample.cafekiosk.spring.domain.order;

import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    
    private final List<Product> products;
    private final LocalDateTime registeredDateTime;
    
    private Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.products = products;
        this.registeredDateTime = registeredDateTime;
    }
    
    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return new Order(products, registeredDateTime);
    }
    
    public List<String> getProductNumbers() {
        return products.stream()
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
    }
    
    public LocalDateTime getRegisteredDateTime() {
        return registeredDateTime;
    }
    
    public int getTotalAmount() {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
} 