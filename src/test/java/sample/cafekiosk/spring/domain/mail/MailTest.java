package sample.cafekiosk.spring.domain.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MailTest {

    @DisplayName("주문을 생성할 때, 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void createMail() {
        // given
        String fromEmail = "cafe@naver.com";
        String toEmail = "admin@naver.com";
        String title = "20250713 매출 보고";
        String content = "00카페의 20250713 총 매출 보고서입니다.";

        // when
        Mail mail = new Mail(fromEmail, toEmail, title, content);

        // then
        assertThat(mail.getFromEmail()).isEqualTo(fromEmail);
        assertThat(mail.getToEmail()).isEqualTo(toEmail);
        assertThat(mail.getTitle()).isEqualTo(title);
        assertThat(mail.getContent()).isEqualTo(content);
    }
}