package sample.cafekiosk.spring.api.controller.product;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.response.APIResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@RequiredArgsConstructor
@RestController
public class ProductController {

  private final ProductService productService;

  @GetMapping("/api/v1/products/selling")
  public ResponseEntity<APIResponse<List<ProductResponse>>> getSellingProducts() {
    List<ProductResponse> products = productService.getSellingProducts();
    APIResponse<List<ProductResponse>> response = APIResponse.<List<ProductResponse>>builder()
        .code(200)
        .status("OK")
        .message("판매 중인 상품 목록입니다.")
        .data(products)
        .build();
    return ResponseEntity.ok(response);
  }
//    @GetMapping("/api/v1/products/selling")
//    public List<ProductResponse> getSellingProducts() {
//        return productService.getSellingProducts();
//    }

  @PostMapping("/api/v1/products")
  public ResponseEntity<APIResponse<ProductResponse>> createProduct(
      @RequestBody @Valid ProductCreateRequest request) {

    ProductResponse product = productService.createProduct(request);
    APIResponse<ProductResponse> response = APIResponse.<ProductResponse>builder()
        .code(200)
        .status("OK")
        .message("상품이 성공적으로 등록되었습니다.")
        .data(product)
        .build();
    return ResponseEntity.ok(response);
  }
}