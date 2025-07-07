package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
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
    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    // 신규 상품 등록
    @Transactional
    public ApiResponse<ProductResponse> createProduct(ProductCreateRequest request) {
        String newProductNumber = generateNextProductNumber();

        Product product = Product.builder()
                .productNumber(newProductNumber)
                .type(request.getType())
                .sellingStatus(ProductSellingStatus.HOLD)
                .name(request.getName())
                .price(request.getPrice())
                .build();
        productRepository.save(product);

        ProductResponse response = ProductResponse.of(product);

        return ApiResponse.onSuccess("200", "새로운 상품이 등록되었습니다.", response);
    }

    // 상품 번호 생성하기
    private String generateNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            latestProductNumber = "000";
        }

        int nextProductNumber = Integer.parseInt(latestProductNumber) + 1; // "007" → 7
        return String.format("%03d", nextProductNumber); // 7 + 1 → "008"
    }
}
