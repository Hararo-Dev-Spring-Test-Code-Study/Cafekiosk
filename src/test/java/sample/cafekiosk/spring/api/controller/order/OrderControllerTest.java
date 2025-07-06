package sample.cafekiosk.spring.api.controller.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sample.cafekiosk.spring.api.controller.exception.GlobalExceptionController;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
class OrderControllerTest {

  @Mock
  private OrderService orderService;

  @InjectMocks
  private OrderController orderController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(orderController)
        .setControllerAdvice(new GlobalExceptionController())
        .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("주문 생성 요청이 정상적으로 처리되어 응답이 반환된다")
  void createOrder() throws Exception {
    OrderResponse response = OrderResponse.builder()
        .registeredDateTime(LocalDateTime.of(2025, 7, 6, 0, 0))
        .totalPrice(8000)
        .products(List.of(
            ProductResponse.builder()
                .productNumber("001")
                .type(ProductType.BOTTLE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("콜라")
                .price(1000)
                .build(),
            ProductResponse.builder()
                .productNumber("002")
                .type(ProductType.BAKERY)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("마늘빵")
                .price(3000)
                .build(),
            ProductResponse.builder()
                .productNumber("003")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build()
        ))
        .build();

    given(orderService.createOrder(any(), any())).willReturn(response);

    OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002", "003"));

    mockMvc.perform(post("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("주문이 성공적으로 생성되었습니다."))
        .andExpect(jsonPath("$.data.totalPrice").value(8000))
        .andExpect(jsonPath("$.data.products").isArray())
        .andExpect(jsonPath("$.data.products.length()").value(3))
        .andExpect(jsonPath("$.data.products[0].productNumber").value("001"))
        .andExpect(jsonPath("$.data.products[1].productNumber").value("002"))
        .andExpect(jsonPath("$.data.products[2].productNumber").value("003"));
  }


  @Test
  @DisplayName("상품 번호가 비어있을 경우, 신규 주문 생성 요청은 실패하고 400 에러를 반환한다")
  void createOrder_invalidProductNumber_shouldReturnBadRequest() throws Exception {
    OrderCreateRequest request = new OrderCreateRequest(List.of());

    mockMvc.perform(post("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(400))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 번호 리스트는 비어 있을 수 없습니다."));
  }
}
