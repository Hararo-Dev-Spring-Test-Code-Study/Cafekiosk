package sample.cafekiosk.spring.domain.mail;

public class MailData {
    private final String from;
    private final String to;
    private final String subject;
    private final String content;

    public MailData(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
}
