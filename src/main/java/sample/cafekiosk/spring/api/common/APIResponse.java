package sample.cafekiosk.spring.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//제네릭 T 사용해서 다양한 응답 데이터 포괄
public class APIResponse<T> {
    private int code;
    private String status;
    private String message;
    private T data;

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(200, "OK", "요청이 성공했습니다.", data);
    }

    public static <T> APIResponse<T> fail(String message) {
        return new APIResponse<>(400, "BAD_REQUEST", message, null);
    }
}