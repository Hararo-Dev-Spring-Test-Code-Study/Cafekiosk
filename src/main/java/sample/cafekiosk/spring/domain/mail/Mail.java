package sample.cafekiosk.spring.domain.mail;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mails")
@Entity
public class Mail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromEmail;
    private String toEmail;
    private String title;
    private String content;

    public Mail(String fromEmail, String toEmail, String title, String content) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.title = title;
        this.content = content;
    }
}
