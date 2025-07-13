package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.time.LocalDate;

class MailSchedulerTest {

    @Test
    void runDailySalesSummary_메일서비스호출_검증() {
        // given: MailService를 Mock 객체로 생성하고 MailScheduler에 주입
        MailService mockMailService = mock(MailService.class);
        MailScheduler scheduler = new MailScheduler(mockMailService);

        // when: 스케줄러 메서드 실행
        scheduler.runDailySalesSummary();

        // then: MailService의 sendSalesSummaryMail이 어제 날짜로 호출되었는지 확인
        verify(mockMailService, times(1)).sendSalesSummaryMail(LocalDate.now().minusDays(1));
    }
}