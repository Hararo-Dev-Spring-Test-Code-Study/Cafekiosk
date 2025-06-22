package sample.cafekiosk.spring.domain.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.ProductCreateRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc를 자동으로 구성함
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @DisplayName("관리자가 신규 상품을 등록한다.")
    @Test
    public void createProduct() throws Exception {
        //given
        ProductCreateRequest request = new ProductCreateRequest(
                "ignored", // 자동 생성되므로 의미 없음
                ProductType.HANDMADE,
                ProductSellingStatus.HOLD,
                "아메리카노",
                4000
        );

        String json = new ObjectMapper().writeValueAsString(request);

        // when, then
        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNumber").exists())
                .andExpect(jsonPath("$.name").value("아메리카노"))
                .andExpect(jsonPath("$.type").value("HANDMADE"))
                .andExpect(jsonPath("$.sellingStatus").value("HOLD"))
                .andExpect(jsonPath("$.price").value(4000));
    }

    // @AfterEach 때문에 사실상 createProduct랑 같은 조건에서 실행됨
    @DisplayName("상품이 아무것도 없을 때 productNumber는 001로 생성된다")
    @Test
    void createFirstProduct() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                "ignored",
                ProductType.HANDMADE,
                ProductSellingStatus.HOLD,
                "아메리카노",
                4000
        );
        String json = new ObjectMapper().writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNumber").value("001"));
    }

    @Test
    @DisplayName("이미 상품이 하나 있는 경우 productNumber는 002로 생성된다")
    void createSecondProduct() throws Exception {
        // given: 먼저 하나 저장
        Product first = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("1번째 상품")
                .price(1000)
                .build();
        productRepository.save(first);

        // 두 번째 상품 생성
        ProductCreateRequest request = new ProductCreateRequest(
                "ignored",
                ProductType.HANDMADE,
                ProductSellingStatus.HOLD,
                "2번째 상품",
                2000
        );
        String json = new ObjectMapper().writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNumber").value("002"));
    }
}
