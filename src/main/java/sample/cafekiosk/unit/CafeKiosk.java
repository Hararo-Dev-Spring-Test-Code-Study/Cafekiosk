package sample.cafekiosk.unit;

import lombok.Getter;
import org.aspectj.weaver.ast.Or;
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
        for (int i = 0; i < count; i++)
            beverages.add(beverage);
    }

    // remove : 음료 1잔 주문 삭제
    public void remove(Beverage beverage) {
        if (beverages.contains(beverage))
            beverages.remove(beverage);
        // 주문하지 않은 음료 삭제
        else {
            throw new IllegalArgumentException("주문 하지 않은 음료입니다.");
        }
    }

    // clear : 주문 전체 삭제
    public void clear() {
        beverages.clear();
    }

    // calculateTotalPrice : 주문한 음료의 총 금액 계산
    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Beverage b : beverages)
            totalPrice += b.getPrice();

        return totalPrice;
    }

    // createOrder : 주문 생성
    // LocalDateTime은 테스트하기 어려워서 외부로 빼서 주문 생성 메서드를 작성하는게 좋음
//    public Order createOrder() {
//        return new Order(LocalDateTime.now(), beverages);
//    }
    public Order createOrder(LocalDateTime orderTime) {
        return new Order(orderTime, beverages);
    }
}
