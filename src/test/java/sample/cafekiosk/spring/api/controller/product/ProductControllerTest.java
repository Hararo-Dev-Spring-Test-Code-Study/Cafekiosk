package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("관리자가 신규 상품을 등록하면 공통 응답값 형태로 반환된다")
    @Test
    public void createProduct() throws Exception {
        //given
        ProductCreateRequest request = new ProductCreateRequest(
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
                .andExpect(jsonPath("$.data.productNumber").exists())
                .andExpect(jsonPath("$.data.productNumber").value("004"))
                .andExpect(jsonPath("$.data.name").value("아메리카노"))
                .andExpect(jsonPath("$.data.type").value("HANDMADE"))
                .andExpect(jsonPath("$.data.sellingStatus").value("HOLD"))
                .andExpect(jsonPath("$.data.price").value(4000));
    }
}
