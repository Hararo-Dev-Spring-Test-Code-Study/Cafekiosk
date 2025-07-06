package sample.cafekiosk.spring.api.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class APIResponseTest {

    @Test
    @DisplayName("성공 응답 포맷이 올바르게 생성되는지 확인")
    void successResponse() {
        // given
        String data = "상품 등록 성공";

        // when
        APIResponse<String> response = APIResponse.success(data);

        // then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo("OK");
        assertThat(response.getMessage()).isEqualTo("요청이 성공했습니다.");
        assertThat(response.getData()).isEqualTo("상품 등록 성공");
    }

    @Test
    @DisplayName("실패 응답 포맷이 올바르게 생성되는지 확인")
    void failResponse() {
        // given
        String errorMessage = "요청이 잘못되었습니다.";

        // when
        APIResponse<String> response = APIResponse.fail(errorMessage);

        // then
        assertThat(response.getCode()).isEqualTo(400);
        assertThat(response.getStatus()).isEqualTo("BAD_REQUEST");
        assertThat(response.getMessage()).isEqualTo(errorMessage);
        assertThat(response.getData()).isNull();
    }
}