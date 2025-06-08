// package sample.cafekiosk.unit.beverage;

// public class Americano implements Beverage {

//     // 아메리카노, 4000원

// }

package sample.cafekiosk.unit.beverage;

// Beverage interface를 implements 해서 Americano class를 정의
public class Americano implements Beverage {

    // @Override를 붙여서 인터페이스 또는 부모 클래스에서 선언된 것을 구현함을 명시
    // 필수는 아니지만 붙이는걸 권장
    @Override
    public String getName() {
        return "아메리카노";
    }

    @Override
    public int getPrice() {
        return 4000;
    }

}

// Americano나 Latte 는 고정된 값(이름,가격)을 반환하므로(상태 고정)
// 멤버 변수를 굳이 만들지 않아도 됨

// Order는 매번 다른 시점과 다른 음료 리스트를 가지므로
// 동적인 상태를 저장할 필요가 있음(다양한 상태 state를 가짐)
// -> 멤버 변수가 필요

// public string name = "아메리카노";
// public int price = 4000;
// 이렇게 하지 않는 이유 : 캡슐화 위반, 무분별한 접근 방지, 불변성 보장(상태관리)