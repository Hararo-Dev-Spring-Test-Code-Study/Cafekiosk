// src/main/java/sample/cafekiosk/unit/CafeKiosk.java
package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {
    // 음료 리스트 생성
    private final List<Beverage> beverages = new ArrayList<>();

    // add : 음료 1잔 추가
    public void add(Beverage beverage) {
        if (beverage == null) {
            throw new IllegalArgumentException("음료는 null일 수 없습니다.");
        }
        beverages.add(beverage);
    }

    // addSeveralBeverages : 음료 여러 잔 추가
    public void addSeveralBeverages(Beverage beverage, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("음료는 1잔 이상이어야 합니다.");
        }
        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    // remove : 음료 1잔 주문 삭제
    public void remove(Beverage beverage) {
        if(!beverages.contains(beverage)){
            throw new IllegalArgumentException("삭제하려는 음료가 주문 목록에 존재하지 않습니다.");
        }
        beverages.remove(beverage);
    }

    // clear : 주문 전체 삭제
    public void clear() {
        beverages.clear();
    }

    // calculateTotalPrice : 주문한 음료의 총 금액 계산
    public int calculateTotalPrice() {
        int totalPrice = 0;

        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }
        return totalPrice;
    }

    // beverages 전체 출력
    public void printBeverages() {
        System.out.println("printBeverages 실행 ");
        for (Beverage beverage : beverages) {
            System.out.println(beverage.getName() + " : " + beverage.getPrice());
        }
    }

    // createOrder : 주문 생성
    public Order createOrder() {
        LocalDateTime orderDateTime = LocalDateTime.now();
        return new Order(orderDateTime, beverages);
    }

}
