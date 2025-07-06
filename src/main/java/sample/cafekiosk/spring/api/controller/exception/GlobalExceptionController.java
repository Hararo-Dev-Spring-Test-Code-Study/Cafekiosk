package sample.cafekiosk.spring.api.controller.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sample.cafekiosk.spring.api.response.APIResponse;

@ControllerAdvice
public class GlobalExceptionController {

  private final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public APIResponse<Object> handleValidationException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> fieldError.getDefaultMessage())
        .collect(Collectors.joining(", "));

    logger.warn("Validation failed: {}", message);

    return APIResponse.<Object>builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .status("BAD_REQUEST")
        .message(message)
        .data(null)
        .build();
  }
}
