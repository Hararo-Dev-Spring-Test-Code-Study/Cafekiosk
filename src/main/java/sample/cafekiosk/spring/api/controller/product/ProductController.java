// 사용자 요청을 받고 응답을 반환하는 입구
// 외부 HTTP 요청을 받아서 DTO로 팧싱하고 Service에 전달한 뒤 Response DTO를 받아서 클라이언트에게 응답

package sample.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

// week 8 추가
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import org.springframework.web.bind.annotation.PostMapping;
// Spring이 자동으로 JSON을 Java 객체로 바인딩
// 메서드 파라미터 앞에 @RequestBody 어노테이션이 있어야 정확하게 동작함
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

    // week 8 추가
    // 새로운 상품 추가하는 엔드포인트
    // 클라이언트가 보낸 HTTP 요청의 본문에 담긴 데이터를 ProductCreateRequest로 받음
    @PostMapping("/api/v1/products/new")
    public void createProduct(@RequestBody ProductCreateRequest request) {
        // 실제 비즈니스 로직은 ProductService에게 위임
        productService.createProduct(request);
    }

}
