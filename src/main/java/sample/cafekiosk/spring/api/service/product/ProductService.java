package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.controller.request.ProductCreateRequest;
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

    //신규상품등록
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // 다음 상품번호 생성 (예: 001, 002, ...)
        String nextProductNumber =generateNextProductNumber();

        Product product = Product.builder()
                .productNumber(nextProductNumber)
                .name(request.getName())
                .type(request.getType())
                .sellingStatus(request.getSellingStatus())
                .price(request.getPrice())
                .build();

        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);

    }

    //고유 상품번호 생성
    private String generateNextProductNumber() {
        String latest = productRepository.findTopByOrderByProductNumberDesc()
                .map(Product::getProductNumber)
                .orElse("000");

        int nextNumber = Integer.parseInt(latest) + 1;
        return String.format("%03d", nextNumber);
    }

}
