// sample.cafekiosk.uni.order 라는 패키지(디렉토리)에 속해 있다
package sample.cafekiosk.unit.order;

// 모든 필드에 대한 getter 메서드를 자동 생성
// 불변성을 위해 @Getter만 쓰고 @Setter는 최소화하는 것이 권장됨
import lombok.Getter;

// final 필드만을 인자로 받는 생성자를 자동 생성
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.unit.beverage.Beverage;

// 날짜와 시간을 저장할 때 사용하는 java 내장 클래스
import java.time.LocalDateTime;
// java 표준 라이브러리에서 제공하는 자료구조 인터페이스
// add(요소): 요소 추가
// get(index): 요소 읽기
// size() 길이 반환
// remove(index): 삭제
import java.util.List;

// 아래와 같은 메서드들을 자동 생성
// public LocalDateTime getOrderDateTime() { return this.orderDateTime; }
// public List<Beverage> getBeverages() { return this.beverages ;}
@Getter
// final 또는 @NonNull 필드만을 인자로 받는 생성자를 자동 생성
// final 필드는 무조건 초기화해야 하는 필드
// public Order(LocalDateTime orderDateTime, List<Beverage> beverages) {
//    this.orderDateTime = orderDateTime;
//    this.beverages = beverages;
//}
@RequiredArgsConstructor
// 주문 정보를 나타내는 Order 객체 클래스 선언
// public class -> 어디서든 접근 가능
public class Order {

    // private -> 외부에서 직접 접근불가(캡슐화)
    // final -> 생성자에서 한 번만 초기화, 이후 변경 불가
    private final LocalDateTime orderDateTime;
    // Beverage 타입의 요소를 담는 리스트
    // [Americano객체, Latte객체]가 들어간 리스트
    // beverages 필드가 생성자를 통해 주입되므로 이미 완성된 List<Beverage>가 외부에서 들어옴
    // 이 때 구체적으로 어떤 List구현체 인지는 중요하지 않아 List라는 추상 타입으로 받는게 더 유연함
    private final List<Beverage> beverages;

}

// java에서는 final이 const 역할
// const 키워드가 존재하긴 하지만 사용되지 않음
// final int a = 10; a의 값을 바꿀 수 없음
// 기본형 final 변수의 값은 바꿀 수 없음 -> compile error
// final 변수는 값을 한번만 할당 가능
// final 메서드는 오버라이딩 방지
// final 클래스는 상속 금지
// 객체를 참조하는 참조형 final 변수는 참조만 불변, 객체 내용은 바뀔 수 있음(const pointer?)
