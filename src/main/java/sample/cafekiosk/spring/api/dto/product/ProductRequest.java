package sample.cafekiosk.spring.api.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {

    private String productNumber;
    private String name;
    private int price;
    private ProductType type;
    private ProductSellingStatus sellingStatus;

    public Product toEntity() {
        return Product.create(productNumber, name, price, type, sellingStatus);
    }
}
