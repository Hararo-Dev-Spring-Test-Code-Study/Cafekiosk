package sample.cafekiosk.spring.domain.mail;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "mail_history")
public class MailHistory {
    @Id
    @GeneratedValue
    private Long id;

    private String from;
    private String to;
    private String subject;
    private String content;
    private LocalDateTime sentAt;

    protected MailHistory() {
    }

    public MailHistory(String from, String to, String subject, String content, LocalDateTime sentAt) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.sentAt = sentAt;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
}
