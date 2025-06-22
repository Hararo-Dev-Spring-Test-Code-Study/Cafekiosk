package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
// 서비스, 레포지토리, DB 등은 로딩하지 않고 오직 컨트롤러만 테스트하기 위한 전용 테스트 환경설정
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

// 컨트롤러가 의존하는 다른 컴포넌트를 가짜 Mock 객체로 대체 -> @Autowired 필드에 주입됨
import org.springframework.boot.test.mock.mockito.MockBean;

// 요청/응답의 MIME 타입 정의(ex. application/json)
import org.springframework.http.MediaType;
// 실제 HTTP 요청처럼 테스트할 수 있는 클래스 MockMvc
import org.springframework.test.web.servlet.MockMvc;

import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

// Mockito 라이브러리를 활용한 가짜 객체 설정
// any() : 어떤 인자가 들어와도 대응
// given(...).willReturn(...) : BDD 스타일로 mock 행위를 정의
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

// HTTP 요청 메서드 생성 및 응답 결과 검증용 정적 메서드
// post(...) : POST 요청 생성
// status().isCreated() : HTTP 상태코드 201 기대
// jsonPath(...) : 응답 본문의 JSON 필드 검증
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest 를 사용해 웹 계층(Controller)만 테스트
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    // MockMvc를 사용해 실제 HTTP 요청처럼 시뮬레이션
    @Autowired
    private MockMvc mockMvc;

    // ProductService는 실제 객체 대신 @MockBean으로 가짜 객체 주입해 테스트
    @MockBean
    private ProductService productService;

    // ProductRepository는 mock 처리할 필요 없음
    // 테스트 대상인 controller가 직접 repositry 사용하지 않기 때문


    // 자바 객체 <-> JSON 변환 담당
    // 요청 본문(JSON문자열) 생성시 사용
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("신규 상품 등록 요청 시 201 응답과 생성된 상품 정보를 반환한다")
    @Test
    void createProduct() throws Exception {
        // given
        // HTTP 요청에 사용할 JSON 요청 객체 생성
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // 실제 서비스가 반환할 응답 객체 정의(Mock 이니까 직접 설정)
        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // productService.createProduct(...)가 호출되면 위에서 만든 응답(response) 반환하도록 설정
        given(productService.createProduct(any())).willReturn(response);

        // when & then
        // MockMvc 사용해 실제 HTTP POST 요청처럼 테스트
        mockMvc.perform(post("/api/v1/products/new")
                        // 요청이 JSON 이라는걸 명시
                        .contentType(MediaType.APPLICATION_JSON)
                        // request 객체를 JSON 문자열로 변환(HTTP 요청의 본문(body)에 해당 문자열 넣는 역할)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 201 Created 예상
                .andExpect(jsonPath("$.productNumber").value("001"))
                .andExpect(jsonPath("$.name").value("카푸치노"))
                .andExpect(jsonPath("$.price").value(5000));
    }
}
