package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MailScheduler {

    private final MailService mailService;

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * ?")
    public void runDailySalesSummary() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        mailService.sendSalesSummaryMail(yesterday);
    }
}
