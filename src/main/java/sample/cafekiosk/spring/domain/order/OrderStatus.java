package sample.cafekiosk.spring.domain.order;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("대기 중"),
    IN_PROGRESS("진행 중"),
    COMPLETED("완료");

    private final String displayName;
}
