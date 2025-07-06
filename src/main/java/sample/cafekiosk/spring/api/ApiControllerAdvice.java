package sample.cafekiosk.spring.api;

// API 요청 중 발생하는 예외를 전역적으로 처리하고 ApiResponse 형식으로 일관된 에러 응답을 반환하는 역할

import org.springframework.http.HttpStatus;
// 유효성 검증 실패 시 발생하는 예외 (@Valid, @Validated 등의 어노테이션 사용시 등)
import org.springframework.validation.BindException;
// 특정 예외 클래스에 대한 처리 메서드임을 명시 -> 해당 예외가 발생했을 때 호출됨
import org.springframework.web.bind.annotation.ExceptionHandler;
// 예외 처리 메서드가 어떤 HTTP 상태 코드로 응답할 지 지정
import org.springframework.web.bind.annotation.ResponseStatus;
// @ControllerAdvice + @ResponseBody의 합성 어노테이션
// 전역 예외 처리용 클래스이며, 모든 @RestController에서 발생하는 예외를 가로채 JSON 형태로 응답함
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    // 아래 메서드가 실행되면 HTTP 상태 코드는 400(BAD_REQEUST)로 설정됨
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // BindException이 발생할 경우 이 메서드가 실행됨
    @ExceptionHandler(BindException.class)
    // 반환 타입은 ApiRespons<Object>로 일관된 API 응답 포맷 사용
    public ApiResponse<Object> bindException(BindException e) {
        // API 응답을 직접 생성하여 반환
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            // 검증 실패 항목 중 첫 번째 오류 메시지를 꺼냄을 의미
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            // data는 실패 응답이므로 null로 설정
            null
        );
    }
}