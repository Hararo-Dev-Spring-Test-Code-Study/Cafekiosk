package sample.cafekiosk.spring.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    // ENUM 필드를 둘때, text로 설명 필드를 생성했습니다.
    HANDMADE("제조 음료"),
    BOTTLE("병음료"),
    BAKERY("베[이커리");

    private final String text;

}
