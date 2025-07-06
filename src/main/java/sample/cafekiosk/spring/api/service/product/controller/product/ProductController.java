package sample.cafekiosk.spring.api.service.product.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.cafekiosk.spring.api.common.APIResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.controller.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/sellingDP")
    public List<Product> getSellingProducts() {
        return productRepository.findAllBySellingStatusIn(
                List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD)
        );
    }

    //상품추가-APIResponse 포맷적용
    @PostMapping
    public APIResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return APIResponse.success(response);
    }
}