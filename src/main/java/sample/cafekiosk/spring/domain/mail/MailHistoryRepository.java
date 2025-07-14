package sample.cafekiosk.spring.domain.mail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailHistoryRepository extends JpaRepository<MailHistory, Long> {
}
