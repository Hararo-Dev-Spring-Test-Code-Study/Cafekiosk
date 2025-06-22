// Service Layer : 비즈니스 로직 처리, 트랜잭션 범위 관리

package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

// week 8 추가
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;

//week 8 추가
// 기본은 읽기 전용 명시
@Transactional(readOnly = true)

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

    // week 8 추가
    // 메서드 레벨 @Transactional 쓰기 작업 별도 선언
    // 상품 저장 중 예외 발생시 전체 작업 자동 롤백
    // 보통 쓰기 작업(INSERT, UPDATE, DELETE)이 있는 메서드에 사용
    @Transactional
    // 상품 생성 요청 처리하는 서비스 메서드
    // ProductCreateRequest DTO(사용자가 보낸 상품 정보) 받아와
    // prodcutResponse DTO(상품이 저장된 결과 응답용) 만들어 반환
    public ProductResponse createProduct(ProductCreateRequest request) {
        // 새로 저장할 상품에 사용할 상품번호 생성
        String nextProductNumber = createNextProductNumber();

        // request DTO에 정의된 toEntity() 호출해 Product 엔티티 객체 생성
        Product product = request.toEntity(nextProductNumber);
        // 생성된 Product 엔티티 DB에 저장하고 저장된 객체가 savedProduct에 반환됨
        // save() 메서드 호출 -> DB에 INSERT 쿼리 실행
        Product savedProduct = productRepository.save(product);

        // of() 정적 팩토리 메서드, 엔티티 -> DTO 변환 역할
        // Product -> ProductResponse
        // 엔티티 id 까지 생김
        return ProductResponse.of(savedProduct);
    }

    // 상품번호 생성 메서드
    private String createNextProductNumber() {
        // findLatestProductNumber() 호출해 가장 마지막에 저장된 상품의 상품번호 가져옴
        String latestProductNumber = productRepository.findLatestProductNumber();
        // 저장된 상품이 아무것도 없을 경우 "001" 반환
        if (latestProductNumber == null) {
            return "001";
        }

        // 연산위해 파싱
        // "001" string -> int 1
        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        // 2 int -> string "002" 세자리 문자열로 반환
        return String.format("%03d", nextProductNumberInt);
    }

}