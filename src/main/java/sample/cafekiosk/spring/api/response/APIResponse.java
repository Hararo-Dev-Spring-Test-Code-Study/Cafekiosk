package sample.cafekiosk.spring.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class APIResponse<T> {

  private final int code;
  private final String status;
  private final String message;
  private final T data;
}