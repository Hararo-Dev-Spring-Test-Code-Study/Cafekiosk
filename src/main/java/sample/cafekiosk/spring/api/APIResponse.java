package sample.cafekiosk.spring.api;

import lombok.Builder;
import lombok.Getter;

@Getter
public class APIResponse<T> {

    private final int code;
    private final String status;
    private final String message;
    private final T data;

    @Builder
    public APIResponse(int code, String status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> APIResponse<T> success(T data) {
        return APIResponse.<T>builder()
                .code(200)
                .status("OK")
                .message("성공")
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> error(int code, String message) {
        return APIResponse.<T>builder()
                .code(code)
                .status("ERROR")
                .message(message)
                .data(null)
                .build();
    }
}