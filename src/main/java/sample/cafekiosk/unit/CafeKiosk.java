// package sample.cafekiosk.unit;

// import lombok.Getter;
// import sample.cafekiosk.unit.beverage.Beverage;
// import sample.cafekiosk.unit.order.Order;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// @Getter
// public class CafeKiosk {

//     // add : 음료 1잔 추가
//     // addSeveralBeverages : 음료 여러 잔 추가
//     // remove : 음료 1잔 주문 삭제
//     // clear : 주문 전체 삭제
//     // calculateTotalPrice : 주문한 음료의 총 금액 계산
//     // createOrder : 주문 생성

// }

// java에서 import는 각 클래스 파일별로 독립적으로 선언해야함
// Order.java에서 import 했더라도 다시 import
// 컴파일러각 각 파일을 독립적으로 처리하기 때문
package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
// List는 인터페이스, ArrayList는 List 인터페이스를 구현(override)한 구체적인 클래스
// 내부적으로 동적 배열 구조를 사용해 데이터 저장
import java.util.ArrayList;
import java.util.List;

// 카페 키오스크 시스템을 나타내는 클래스
// 주문할 음료를 담는 리스트인 beverages 필드
@Getter
// public List<beverages> getBeverages() { return this.beverages; }
public class CafeKiosk {

    // 다른 List 구현체로 바꿀 수 있어 유연성이 좋아짐
    // List<Beverage> beverages = new LinkedList<>();
    // CafeKiosk 클래스에서는 beverages 필드를 내부에서 직접 생성하고 관리하기 때문에
    // 구체적인 구현체인 ArrayList를 바로 생성해서 사용

    // 외부에서 받는 경우는 인터페이스, 내부에서 생성하는 경우는 구체 클래스를 생성하지만
    // 선언은 인터페이스로 하는게 관례

    // final 필드는 생성자에서 초기화하거나 필드 선언과 동시에 초기화 해야함

    // ArrayList<Beverage> -> java 7부터 ArrayList<> 다이아몬드 연산자(<>)로 제네릭 타입 생략 가능
    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    // add() 메서드 오버로딩으로 addSeveralBeverages() 기능
    public void add(Beverage beverage, int count) {
        if (count <= 0) {

            // IllegalArgumentException은 java 표준 라이브러리(java.lang패키지)에 포함된 예외 클래스
            // import문 없이 바로 사용 가능
            // 메서드에 전달된 인자가 부적절할 때 던지는 런타임 예외 객체
            // RuntimeException의 하위 클래스
            // RuntimeException은 Exception의 하위 클래스

            // 현재 메서드 실행 즉시 멈추고 메서드를 호출한 곳으로 예외 전달
            // 호출자도 예외처리하지 않으면 그 위로 계속 전파 -> exception propagation
            // 최종적으로 예외를 잡아 처리하는 코드(try-catch) 있다면 그곳에서 처리
            // 어디에서도 처리하지 않으면 프로그램 비정상 종료되고 예외메시지와 스택 trace 출력됨
            throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");
        }

        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    // 리스트를 앞에서부터 탐색하면서 첫 번째로 만나는 beverage 객체 삭제
    // 나머지 요소들은 앞당겨져서 빈 자리가 메워짐
    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }
        return totalPrice;
    }

    // CafeKiosk 클래스 안에서 주문(Order) 객체를 새로 만드는 메서드
    // Order객체 생성자에 주문시간, beverages를 인자로 넘겨줌
    // 키오스크에서 음료를 추가/삭제 하다가 주문이 확정될 때
    // createOrder()해서 Order 객체 만들어 반환하는 구조
    public Order createOrder() {
        return new Order(LocalDateTime.now(), beverages);
    }

}