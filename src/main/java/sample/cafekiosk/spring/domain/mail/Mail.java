package sample.cafekiosk.spring.domain.mail;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@Entity
// - 메일을 전송시 포함되는 데이터는 보내는 email, 받는 email, email 제목, 내용입니다.
public class Mail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fromMail;
  private String toMail;
  private String title;


  private String content;

  private Integer totalAmount;

  // - 메일을 전송한 기록을 남긴다.
  private LocalDateTime sentDate;

  @Builder
  public Mail(String fromMail, String toMail, String title, String content, Integer totalAmount,
      LocalDateTime sentDate) {
    this.fromMail = fromMail;
    this.toMail = toMail;
    this.title = title;
    this.content = content;
    this.totalAmount = totalAmount;
    this.sentDate = sentDate != null ? sentDate : LocalDateTime.now();
  }
}