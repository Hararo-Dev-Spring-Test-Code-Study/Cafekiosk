package sample.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("새로운 주문을 생성하면 공통 응답값 형태로 반환된다")
    @Test
    void createOrder() throws Exception {
        // given
        List<String> productNumbers = List.of("001", "002");
        OrderCreateRequest request = new OrderCreateRequest(productNumbers);

        // 응답용 ProductResponse 리스트 생성
        List<ProductResponse> productResponses = List.of(
                ProductResponse.builder()
                        .id(1L)
                        .productNumber("001")
                        .type(ProductType.HANDMADE)
                        .sellingStatus(ProductSellingStatus.SELLING)
                        .name("아메리카노")
                        .price(4000)
                        .build(),
                ProductResponse.builder()
                        .id(2L)
                        .productNumber("002")
                        .type(ProductType.HANDMADE)
                        .sellingStatus(ProductSellingStatus.HOLD)
                        .name("카페라떼")
                        .price(4500)
                        .build()
        );

        OrderResponse orderResponse = OrderResponse.builder()
                .id(1L)
                .registeredDateTime(LocalDateTime.now())
                .totalPrice(8500)
                .products(productResponses)
                .build();

        ApiResponse<OrderResponse> apiResponse = ApiResponse.onSuccess("200", "주문이 등록되었습니다.", orderResponse);

        // OrderService.createOrder() 메서드가 어떤 인자를 받더라도 위 응답을 리턴하도록 설정
        when(orderService.createOrder(any(OrderCreateRequest.class), any(LocalDateTime.class)))
                .thenReturn(apiResponse);

        // when & then
        mockMvc.perform(post("/api/v1/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("주문이 등록되었습니다."))
                .andExpect(jsonPath("$.data.totalPrice").value(8500))
                .andExpect(jsonPath("$.data.products.length()").value(2));
    }
}