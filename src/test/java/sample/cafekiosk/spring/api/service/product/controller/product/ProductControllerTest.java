package sample.cafekiosk.spring.api.service.product.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.service.product.controller.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("신규 상품을 등록할 수 있다")
    void createProduct() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                "수박주스",
                ProductType.HANDMADE,
                ProductSellingStatus.SELLING,
                4500
        );
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productNumber").value("004"))
                .andExpect(jsonPath("$.data.name").value("수박주스"))
                .andExpect(jsonPath("$.data.type").value("HANDMADE"))
                .andExpect(jsonPath("$.data.sellingStatus").value("SELLING"))
                .andExpect(jsonPath("$.data.price").value(4500));
    }
}