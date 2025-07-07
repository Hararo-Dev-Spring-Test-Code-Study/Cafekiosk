package sample.cafekiosk.spring.api.service.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.controller.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("신규 상품을 등록하고 주문을 생성할 수 있다")
    void createOrder() throws Exception {
        // given: 테스트용 신규 상품 2개 생성 및 저장
        ProductCreateRequest request1 = new ProductCreateRequest(
                "자몽에이드",
                ProductType.HANDMADE,
                ProductSellingStatus.SELLING,
                5500
        );
        ProductCreateRequest request2 = new ProductCreateRequest(
                "레몬에이드",
                ProductType.HANDMADE,
                ProductSellingStatus.SELLING,
                5300
        );

        // 서비스 계층을 통해 실제 상품 등록
        ProductResponse saved1 = productService.createProduct(request1);
        ProductResponse saved2 = productService.createProduct(request2);

        // CreateOrderRequest에 등록된 상품의 상품번호를 전달
        CreateOrderRequest orderRequest = new CreateOrderRequest(
                List.of(saved1.getProductNumber(), saved2.getProductNumber())
        );
        String json = objectMapper.writeValueAsString(orderRequest);

        // when & then: 주문 생성 요청 → 응답 검증
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.totalPrice").value(5500 + 5300)) // 총 금액 검증
                .andExpect(jsonPath("$.data.products.length()").value(2)); // 주문 상품 수 검증
    }
}