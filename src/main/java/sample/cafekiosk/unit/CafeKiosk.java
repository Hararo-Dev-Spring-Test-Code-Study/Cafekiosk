package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    private final List<Beverage> beverages = new ArrayList<>();

    // add : 음료 1잔 추가
    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    // addSeveralBeverages : 음료 여러 잔 추가
    public void addSeveralBeverages(Beverage beverage, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("1잔 이상 주문해야 합니다.");
        }
        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    // remove : 음료 1잔 주문 삭제
    public void remove(Beverage beverage) {
        beverages.remove(beverage); // 동일한 인스턴스 하나 제거
    }

    // clear : 주문 전체 삭제
    public void clear() {
        beverages.clear();
    }

    // calculateTotalPrice : 주문한 음료의 총 금액 계산
    public int calculateTotalPrice() {
        return beverages.stream()
                .mapToInt(Beverage::getPrice)
                .sum();
    }

    // createOrder : 주문 생성
    public Order createOrder(LocalDateTime orderTime) {
        return new Order(orderTime, new ArrayList<>(beverages));
    }
}
