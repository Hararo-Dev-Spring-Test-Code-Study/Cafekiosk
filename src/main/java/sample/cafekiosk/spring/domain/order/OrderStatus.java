package sample.cafekiosk.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    CANCEL("주문취소"),
    ORDER("주문접수");

    private final String text;

    public static List<OrderStatus> forDisplay() {
        return List.of(CANCEL, ORDER);
    }
}