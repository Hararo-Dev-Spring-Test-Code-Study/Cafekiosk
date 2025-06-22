package sample.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.cafekiosk.spring.api.dto.product.ProductRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.dto.product.ProductResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 판매중 상품 조회
    @GetMapping("/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

    // 신규 상품 등록
    @PostMapping("/new")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
        String productNumber = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("상품이 등록되었습니다. 번호: " + productNumber);
    }
}
