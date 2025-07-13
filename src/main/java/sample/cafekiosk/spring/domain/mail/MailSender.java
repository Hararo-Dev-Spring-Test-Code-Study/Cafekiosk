package sample.cafekiosk.spring.domain.mail;

public interface MailSender {

  void send(String from, String to, String title, String content);
}
