package sample.cafekiosk.spring.api.controller.product; // ✨ 패키지 경로 확인! ✨

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.service.product.ProductService; // ✨ ProductService 임포트 경로 확인! ✨
import sample.cafekiosk.spring.api.service.product.response.ProductResponse; // ✨ ProductResponse 임포트 경로 확인! ✨

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products/selling") // ✨ 이 부분에서 URL 매핑! ✨
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }
}
