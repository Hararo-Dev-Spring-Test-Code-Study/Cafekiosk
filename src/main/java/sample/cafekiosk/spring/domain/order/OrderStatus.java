package sample.cafekiosk.spring.domain.order;

public enum OrderStatus {
    INIT,       // 주문 생성 직후 상태
    COMPLETED,  // 주문 완료
    CANCELLED   // 주문 취소
}
