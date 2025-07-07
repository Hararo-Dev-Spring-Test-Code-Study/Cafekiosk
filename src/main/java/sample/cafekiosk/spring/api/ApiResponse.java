package sample.cafekiosk.spring.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "status", "message", "data"}) // 직렬화 하는 속성 순서 정함
public class ApiResponse <T> {

    private static final String DEFAULT_SUCCESS_CODE = "S001";
    private static final String DEFAULT_SUCCESS_MESSAGE = "요청 성공";

    private final String code;
    private final Boolean status;
    private final String message;
    private T data;

    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(DEFAULT_SUCCESS_CODE, true, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static <T> ApiResponse<T> onSuccess(String code, String message, T data) {
        return new ApiResponse<>(code, true, message, data);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message) {
        return new ApiResponse<>(code, false, message, null);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(code, false, message, data);
    }
}
