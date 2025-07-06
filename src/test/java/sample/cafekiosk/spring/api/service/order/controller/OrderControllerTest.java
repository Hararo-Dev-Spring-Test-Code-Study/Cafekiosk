package sample.cafekiosk.spring.api.service.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.service.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 생성 요청을 보내면 주문이 생성되고 응답값이 반환된다")
    void createOrder() throws Exception {
        // given
        // 테스트용 상품을 DB에 저장
        Product product1 = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카페라떼")
                .price(4500)
                .build();

        productRepository.saveAll(List.of(product1, product2));

        // CreateOrderRequest 객체 생성 (상품번호 리스트 포함)
        CreateOrderRequest request = new CreateOrderRequest(List.of("001", "002"));
        String json = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()) // 응답 상태 코드 200 OK 검증
                .andExpect(jsonPath("$.code").value(200)) // APIResponse 응답 포맷 검증
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.totalPrice").value(8500)) // 총 가격 검증
                .andExpect(jsonPath("$.data.products.length()").value(2)); // 주문한 상품 개수 검증
    }
}