package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true) // 조회 : 읽기 전용 가능
  // 프론트 화면에서 조회를 했을 때, 판매가 가능한 상태의 제품을 보여준다
  public List<ProductResponse> getSellingProducts() {
    List<Product> products = productRepository.findAllBySellingStatusIn(
        ProductSellingStatus.forDisplay());

    return products.stream()
        .map(ProductResponse::of)
        .collect(Collectors.toList());
  }

  @Transactional
  public ProductResponse createProduct(ProductCreateRequest request) {
    try {
      Product product = Product.builder()
          .productNumber(createNextProductNumber())
          .type(request.getType())
          .sellingStatus(request.getSellingStatus())
          .name(request.getName())
          .price(request.getPrice())
          .build();
      productRepository.save(product);
      return ProductResponse.of(product);
    } catch (DataIntegrityViolationException e) {
      throw new IllegalStateException("이미 존재하는 상품 번호입니다.");
    }
  }

  private String createNextProductNumber() {
    return productRepository.findFirstByOrderByIdDesc() // 가장 마지막에 저장된 상품
        .map(Product::getProductNumber) // 상품이 있으면 productNumber 를 가져오고
        .map(
            num -> String.format("%03d", Integer.parseInt(num) + 1)) // 포매팅 "001" → 1 → 2 → "002" 변환
        .orElse("001"); // 없으면 "001" 로
  }

}
