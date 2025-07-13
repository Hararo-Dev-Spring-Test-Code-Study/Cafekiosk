package sample.cafekiosk.spring.domain.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "test"})
public class StubMailSender implements MailSender {

  private static final Logger log = LoggerFactory.getLogger(StubMailSender.class);

  @Override
  public void send(String from, String to, String title, String content) {
    log.info("메일 전송 (Stub) -> FROM:{}, TO:{}, TITLE:{}, CONTENT:{}",
        from, to, title, content);
  }
}