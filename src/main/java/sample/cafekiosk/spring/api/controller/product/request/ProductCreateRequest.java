package sample.cafekiosk.spring.api.controller.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    private ProductType type;
    private ProductSellingStatus status;
    private String name;
    private int price;
}
