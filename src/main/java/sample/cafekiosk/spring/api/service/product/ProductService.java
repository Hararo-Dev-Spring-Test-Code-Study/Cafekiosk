package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.product.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 프론트 화면에서 조회를 했을 때, 판매가 가능한 상태의 제품을 보여준다
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

    }

    public ProductResponse createProduct(ProductCreateRequest request) {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            latestProductNumber = "000";
        }
        String newProductNumber = generateNextProductNumber(latestProductNumber);

        Product product = Product.builder()
                .productNumber(newProductNumber)
                .type(request.getType())
                .sellingStatus(ProductSellingStatus.HOLD)
                .name(request.getName())
                .price(request.getPrice())
                .build();
        productRepository.save(product);

        // 응답 객체 생성
        return ProductResponse.of(product);
    }

    private String generateNextProductNumber(String latest) {
        int num = Integer.parseInt(latest); // "007" → 7
        return String.format("%03d", num + 1); // 7 + 1 → "008"
    }
}
