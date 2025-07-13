package sample.cafekiosk.spring.api.controller.mail;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.service.mail.MailService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

  private final MailService mailService;

  @GetMapping("/sales")
  public ApiResponse<Integer> sendSalesMail(
      @RequestParam("date")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

    int total = mailService.sendDailySalesMail(
        date,
        "from@from.from",
        "to@to.to"
    );

    return ApiResponse.ok(total);
  }
}
