package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

// 서비스 상단에 readOnly = true를 걸고
// CUD 작업이 있다면 메서드 상단에 transactional를 거는걸 추천
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 여기도 동시성 이슈가 발생할 수 있는 부분
    // 증가하는 형태의 번호를 부여해야하는데, 여러 명의 관리자가 등록한다면?
    // db의 필드에 unique 제약 조건을 걸고 재시도하는 로직을 추가할 수도 있다. -> 빈도수가 높지 않은 경우(시스템에서 재시도)
    // 조금 더 크리티컬한 경우 UUID를 적용
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        // productNumber를 부여
        // 다른 레이어 테스트할 때, 프로덕트에 대한 Number를 세자리 숫자로 임의로 진행했는데 이를 구현해보려 함
        // DB에서 마지막 저장된 product의 상품번호를 읽어와서 +1 해준다.

        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }


    // 상품을 조회 -> 딱 조회만 작동
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
