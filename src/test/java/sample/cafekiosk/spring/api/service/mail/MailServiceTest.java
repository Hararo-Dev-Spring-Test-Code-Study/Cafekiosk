package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @AfterEach
    void tearDown() {
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("메일 전송")
    @Test
    void sendMail() {
        // given
        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "메일 제목";
        String content = "메일 내용";

        // when
        boolean result = mailService.sendMail(fromEmail, toEmail, subject, content);

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("fromEmail", "toEmail", "subject", "content")
                .containsExactlyInAnyOrder(
                        tuple(fromEmail, toEmail, subject, content)
                );
    }
}