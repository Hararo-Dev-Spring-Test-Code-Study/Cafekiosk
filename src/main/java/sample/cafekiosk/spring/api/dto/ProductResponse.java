package sample.cafekiosk.spring.api.dto;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    public static ProductResponse from(Product product) {
        ProductResponse response = new ProductResponse();
        response.id = product.getId();
        response.productNumber = product.getProductNumber();
        response.type = product.getType();
        response.sellingStatus = product.getSellingStatus();
        response.name = product.getName();
        response.price = product.getPrice();
        return response;
    }

    // getter 생략
}
