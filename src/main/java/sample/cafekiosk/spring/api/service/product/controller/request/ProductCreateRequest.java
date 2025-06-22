package sample.cafekiosk.spring.api.service.product.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    private String name;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private int price;
}