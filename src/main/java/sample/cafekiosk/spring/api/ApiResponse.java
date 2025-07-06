package sample.cafekiosk.spring.api;

// WEEK 8 - API 응답 공통 포맷을 정의한 클래스

import lombok.Getter;
// HTTP 상태코드(enum)를 사용하기 위해 import (예: HttpStatus.OK, HttpStatus.BAD_REQUEST 등)
import org.springframework.http.HttpStatus;

@Getter
// API 응답을 감싸는 제네릭 클래스
// <T> 는 응답 데이터의 타입을 의미 (예: ApiResponse<UserDTO>, ApiResponse<List<Product>>)
public class ApiResponse<T> {

    // HTTP 상태 코드를 정수형 코드로 저장(ex: 200, 400, 500), HttpStatus.value()로부터 받아옴
    private int code;
    // HTTP 상태를 HttpStatus Enum 객체로 저장(ex: OK, BAD_REQUEST)
    private HttpStatus status;
    private String message;
    // 실제 응답 데이터(제네릭 타입 T로 정의)
    private T data;

    // 생성자
    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 정적 팩토리 메서드 -> 외부에서 new 키워드 없이 쉽게 객체 생성 가능
    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    // 메시지를 직접 주지 않아도 되는 오버로드 메서드
    // 메시지는 HttpStatus.name() ex: OK, BAD_REQUEST 자동 설정
    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    // 성공 응답 전용 헬퍼 메서드 -> 주로 컨트롤러에서 사용
    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

}
