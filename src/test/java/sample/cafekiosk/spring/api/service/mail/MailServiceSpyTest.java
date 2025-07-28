package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceSpyTest {

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @Spy
    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 - Spy 기반")
    @Test
    void sendMail() {
        // given
        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "제목";
        String content = "내용";

        MailSendHistory dummyHistory = MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .content(content)
                .build();

        when(mailSendHistoryRepository.save(any(MailSendHistory.class)))
                .thenReturn(dummyHistory); // stub

        // when - mailService의 실제 sendMail 메서드가 호출(Spy)
        boolean result = mailService.sendMail(fromEmail, toEmail, subject, content);

        // then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

    @DisplayName("메일 전송 성공시나리오 - Spy로 메소드 stubbing")
    @Test
    void sendMailSuccess() {
        // given
        MailService spyService = spy(new MailService(mailSendHistoryRepository)); // 직접 만든 spy 객체

        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "제목";
        String content = "내용";

        // 실제 메소드를 호출하지 않고 true를 반환하도록 stubbing
        doReturn(true).when(spyService).sendMail(fromEmail, toEmail, subject, content);

        // when
        boolean result = spyService.sendMail(fromEmail, toEmail, subject, content); // 위에서 지정한 스텁 결과 리턴

        // then
        assertThat(result).isTrue();
        verify(spyService, times(1)).sendMail(fromEmail, toEmail, subject, content);
    }

    @DisplayName("메일 전송 실패 시나리오 - Spy로 메소드 stubbing")
    @Test
    void sendMailFailure() {
        // given
        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "제목";
        String content = "내용";

        // 실제 메소드를 호출하지 않고 false를 반환하도록 stubbing
        doReturn(false).when(mailService).sendMail(fromEmail, toEmail, subject, content);

        // when
        boolean result = mailService.sendMail(fromEmail, toEmail, subject, content);

        // then
        assertThat(result).isFalse();
        verify(mailService, times(1)).sendMail(fromEmail, toEmail, subject, content);
    }
}
