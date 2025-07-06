package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
class ProductControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductController productController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(productController)
        .setControllerAdvice(new GlobalExceptionController())
        .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("신규 상품 등록 요청 시 정상적으로 상품이 등록되고 응답이 반환된다")
  void createProduct() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest(
        ProductType.HANDMADE,
        ProductSellingStatus.SELLING,
        "죠리퐁라떼",
        5500
    );

    String nextProductNumber = "001";

    ProductResponse response = ProductResponse.builder()
        .productNumber(nextProductNumber)
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("죠리퐁라떼")
        .price(5500)
        .build();

    given(productService.createProduct(any(ProductCreateRequest.class)))
        .willReturn(response);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("상품이 성공적으로 등록되었습니다."))
        .andExpect(jsonPath("$.data.productNumber").value(nextProductNumber))
        .andExpect(jsonPath("$.data.name").value("죠리퐁라떼"))
        .andExpect(jsonPath("$.data.price").value(5500));
  }

  @Test
  @DisplayName("상품 이름이 비어있는 경우, 신규 상품 등록 요청은 실패하고 400 에러를 반환한다")
  void createProduct_invalidName_shouldReturnBadRequest() throws Exception {
    // given : 상품 이름을 빈 값으로 추가
    ProductCreateRequest request =
        new ProductCreateRequest(ProductType.HANDMADE, ProductSellingStatus.SELLING, "", 4000);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("상품 이름은 비어 있을 수 없습니다."));
  }

  @Test
  @DisplayName("상품 가격이 0원 미만일 경우, 신규 상품 등록 요청은 실패하고 400 에러를 반환한다")
  void createProduct_invalidPrice_shouldReturnBadRequest() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest(
        ProductType.HANDMADE,
        ProductSellingStatus.SELLING,
        "유자차",
        -1000
    );

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("가격은 0원 이상이어야 합니다."));
  }

  @Test
  @DisplayName("상품 이름이 비어있고, 상품 가격이 0원 미만일 경우, 신규 상품 등록 요청은 실패하고 400 에러를 반환하며, 에러 메시지가 정상적으로 출력된다.")
  void createProduct_invalidPriceAndName_shouldReturnBadRequest() throws Exception {
    ProductCreateRequest request = new ProductCreateRequest(
        ProductType.HANDMADE,
        ProductSellingStatus.SELLING,
        "",
        -1000
    );

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(Matchers.containsString("상품 이름은 비어 있을 수 없습니다.")))
        .andExpect(jsonPath("$.message").value(Matchers.containsString("가격은 0원 이상이어야 합니다.")));
  }
}