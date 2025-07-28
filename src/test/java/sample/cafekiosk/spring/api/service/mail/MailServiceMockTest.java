package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceMockTest {

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 - Mock 기반")
    @Test
    void sendMail() {
        // given
        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "메일 제목";
        String content = "메일 내용";

        MailSendHistory savedHistory = MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .content(content)
                .build();

        // stub, mailSendHistoryRepository.save()가 호출되면 savedHistory를 반환
        // 현재 로직에서는 mailSendHistoryRepository.save() 반환값을 사용하는 부분이 없기 때문에 유의미하진 않음
        when(mailSendHistoryRepository.save(any(MailSendHistory.class)))
                .thenReturn(savedHistory);

        // when
        boolean result = mailService.sendMail(fromEmail, toEmail, subject, content);

        // then
        assertThat(result).isTrue();

        // ArgumentCaptor - mock 객체의 메서드에 전달된 인자(argument)를 캡처해서 검증
        ArgumentCaptor<MailSendHistory> captor = ArgumentCaptor.forClass(MailSendHistory.class);
        verify(mailSendHistoryRepository).save(captor.capture());

        MailSendHistory capturedHistory = captor.getValue();
        assertThat(capturedHistory.getFromEmail()).isEqualTo(fromEmail);
        assertThat(capturedHistory.getToEmail()).isEqualTo(toEmail);
        assertThat(capturedHistory.getSubject()).isEqualTo(subject);
        assertThat(capturedHistory.getContent()).isEqualTo(content);
    }

    @DisplayName("메일 전송시 repository 호출 검증")
    @Test
    void sendMailVerifyRepositoryCall() {
        // given
        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "메일 제목";
        String content = "메일 내용";

        // when
        boolean result =  mailService.sendMail(fromEmail, toEmail, subject, content);

        // then
        // 1. assertThat(result).isTrue() : 실제 결과값 검증 -> 기능 정상 여부 확인
        assertThat(result).isTrue();
        // 2. verify(...) : 호출된 메서드 검증 -> 행위 검증
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
        // 3. verifyNoMoreInteractions(...) : 불필요한 호출 없는지 검증
        verifyNoMoreInteractions(mailSendHistoryRepository);
    }

    // when(...) -> given(), verify(...) -> then(...).should()
    @DisplayName("메일 전송 - BDDMockito")
    @Test
    void sendMail_BDD() {
        // given
        String fromEmail = "from@test.com";
        String toEmail = "to@test.com";
        String subject = "메일 제목";
        String content = "메일 내용";

        MailSendHistory savedHistory = MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .content(content)
                .build();

        // stub (given)
        given(mailSendHistoryRepository.save(any(MailSendHistory.class)))
                .willReturn(savedHistory);

        // when
        boolean result = mailService.sendMail(fromEmail, toEmail, subject, content);

        // then
        assertThat(result).isTrue();

        // verify (then)
        then(mailSendHistoryRepository).should(times(1)).save(any(MailSendHistory.class));
        then(mailSendHistoryRepository).shouldHaveNoMoreInteractions();
    }
}