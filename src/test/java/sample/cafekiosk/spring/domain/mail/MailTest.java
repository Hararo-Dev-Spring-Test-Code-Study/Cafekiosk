package sample.cafekiosk.spring.domain.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MailTest {

    @DisplayName("메일 엔티티 생성 및 필드 확인")
    @Test
    void createMail_success() {
        // given
        String from = "admin@example.com";
        String to = "jin@example.com";
        String subject = "[매출 요약] 2025-07-13";
        String content = "총 매출 합계는 15000원입니다.";
        LocalDateTime now = LocalDateTime.now();

        // when
        Mail mail = new Mail();
        mail.setFromEmail(from);
        mail.setToEmail(to);
        mail.setSubject(subject);
        mail.setContent(content);
        mail.setSentAt(now);

        // then
        assertThat(mail.getFromEmail()).isEqualTo(from);
        assertThat(mail.getToEmail()).isEqualTo(to);
        assertThat(mail.getSubject()).isEqualTo(subject);
        assertThat(mail.getContent()).isEqualTo(content);
        assertThat(mail.getSentAt()).isEqualTo(now);
    }
}