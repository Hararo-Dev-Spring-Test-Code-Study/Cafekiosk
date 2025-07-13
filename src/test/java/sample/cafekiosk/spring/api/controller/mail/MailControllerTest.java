package sample.cafekiosk.spring.api.controller.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.service.mail.MailService;

@WebMvcTest(MailController.class)
class MailControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  MailService mailService;

  @Test
  @DisplayName("일일 매출 메일 전송 API가 200 OK 와 총액을 응답한다.")
  void sendSalesMail() throws Exception {
    // given
    when(mailService.sendDailySalesMail(any(), any(), any()))
        .thenReturn(15000);

    // when & then
    mockMvc.perform(get("/api/v1/mail/sales")
            .param("date", "2025-07-14"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.data").value(15000))
        .andExpect(jsonPath("$.status").value("OK"));
    verify(mailService).sendDailySalesMail(any(), any(), any());
  }


  @Test
  @DisplayName("date 가 없으면 400 Bad Request를 반환한다")
  void sendSalesMail_missingDate_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/api/v1/mail/sales"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("잘못된 날짜 형식이면 400 Bad Request를 반환한다")
  void sendSalesMail_invalidDate_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/api/v1/mail/sales")
            .param("date", "2025-07-32"))
        .andExpect(status().isBadRequest());
  }
}