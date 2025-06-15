package sample.cafekiosk.spring.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Week 7 추가
import java.util.List;

@Getter
@RequiredArgsConstructor

// Java에서 enum은 final 클래스로 컴파일되는 특수한 클래스
// 멤버 변수, 생성자(private만 가능) 정의 가능
// 메서드, 인터페이스, 추상 메서드 구현 가능
public enum ProductType {

    // ENUM 필드를 둘때, text로 설명 필드를 생성했습니다.
    HANDMADE("제조 음료"),
    BOTTLE("병 음료"),
    BAKERY("베이커리");

    private final String text;

    // Week 7 추가
    // 인자로 받는 ProductType이 재고를 필요로 하는 타입인지 판단
    public static boolean containsStockType(ProductType type) { return List.of(BOTTLE, BAKERY).contains(type);}

}

// List.of(param1, param2) -> param1, param2로 구성된 수정 불가능한 리스트를 만들어줌
// List.of(BOTTLE, BAKERY).contains(type) -> type 객체가 BOTTLE이거나 BAKERY이면 true 아니면 false