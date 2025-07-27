package sample.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.order.request.OrderStatisticsRequest;
import sample.cafekiosk.spring.api.service.order.OrderStatisticsService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderStatisticsController.class)
class OrderStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderStatisticsService orderStatisticsService;

    @DisplayName("신규 매출 통계 메일 전송")
    @Test
    void sendOrderStatistics() throws Exception {
        // given
        OrderStatisticsRequest request = OrderStatisticsRequest.builder()
                .orderDate(LocalDate.of(2023, 3, 5))
                .email("test@test.com")
                .build();

        when(orderStatisticsService.sendOrderStatisticsMail(any(LocalDate.class), anyString()))
                .thenReturn(true);

        // when // then
        mockMvc.perform(
                        post("/api/v1/orders/statistics")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").value(true));
    }

    @DisplayName("매출 통계 메일 전송시 주문 날짜는 필수값이다.")
    @Test
    void sendOrderStatisticsWithoutOrderDate() throws Exception {
        // given
        OrderStatisticsRequest request = OrderStatisticsRequest.builder()
                .email("test@test.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/orders/statistics")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("주문 날짜는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("매출 통계 메일 전송시 이메일은 필수값이다.")
    @Test
    void sendOrderStatisticsWithoutEmail() throws Exception {
        // given
        OrderStatisticsRequest request = OrderStatisticsRequest.builder()
                .orderDate(LocalDate.of(2023, 3, 5))
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/orders/statistics")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("매출 통계 메일 전송시 올바른 이메일 형식이어야 한다.")
    @Test
    void sendOrderStatisticsWithInvalidEmail() throws Exception {
        // given
        OrderStatisticsRequest request = OrderStatisticsRequest.builder()
                .orderDate(LocalDate.of(2023, 3, 5))
                .email("invalidEmail")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/orders/statistics")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("올바른 이메일 형식이 아닙니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}