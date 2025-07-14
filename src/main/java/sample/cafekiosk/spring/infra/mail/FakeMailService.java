package sample.cafekiosk.spring.infra.mail;

import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.mail.MailData;
import sample.cafekiosk.spring.domain.mail.MailHistory;
import sample.cafekiosk.spring.domain.mail.MailHistoryRepository;

import java.time.LocalDateTime;

@Component
public class FakeMailService {

    private final MailHistoryRepository mailHistoryRepository;

    public FakeMailService(MailHistoryRepository mailHistoryRepository) {
        this.mailHistoryRepository = mailHistoryRepository;
    }

    public void sendMail(MailData mailData) {
        // 실제 메일 전송은 하지 않음
        System.out.println("메일 전송(가짜): " + mailData.getTo());

        MailHistory history = new MailHistory(
                mailData.getFrom(),
                mailData.getTo(),
                mailData.getSubject(),
                mailData.getContent(),
                LocalDateTime.now()
        );
        mailHistoryRepository.save(history);
    }
}
