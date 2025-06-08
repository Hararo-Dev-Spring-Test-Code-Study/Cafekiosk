package sample.cafekiosk.unit;

// 직접 사용하는 타입만 import하면 됨 -> Beverage는 import 필요없음
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

// 클래스에 main 메서드가 하나라도 있으면 IDE가 실행 가능한 클래스(엔트리포인트)로 판단
public class CafeKioskRunner {

    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println(">>> 아메리카노 추가");
        cafeKiosk.add(new Latte());
        System.out.println(">>> 라떼 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문가격 : " + totalPrice);
    }

}