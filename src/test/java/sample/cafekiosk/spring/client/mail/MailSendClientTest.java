package sample.cafekiosk.spring.client.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MailSendClientTest {

    // @Spy
    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        // Annotation을 사용하지 않고 Mock 객체 생성
        // MailSendClient mailSendClient = mock(MailSendClient.class);
        // MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
        // MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        // when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        //        .thenReturn(true);

        // Spy를 사용해서 검증할 때
        // Mockito의 Spy는 실체 객체를 기반으로 만들어지기 때문에 위의 when을 쓸 수 없다.
        // doReturn(true)
        //        .when(mailSendClient)
        //        .sendEmail(anyString(), anyString(), anyString(), anyString());

        given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        // verify(mailSendHistoryRepository, times(1)).save(ArgumentMatchers.any(MailSendHistory.class));

        // BDDMockito를 사용한 예시
        then(mailSendHistoryRepository).should(times(1)).save(ArgumentMatchers.any(MailSendHistory.class));
    }

}