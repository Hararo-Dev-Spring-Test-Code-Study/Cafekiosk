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
            throw new IllegalArgumentException("1잔 이상 추가해야 합니다.");
        }
        //반복문으로 음료 하나씩 추가
        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    // remove : 음료 1잔 주문 삭제
    public void remove(Beverage beverage) {
        //삭제하려는 음료가 주문한 음료 목록 없을때
        if (!beverages.contains(beverage)) {
            throw new IllegalArgumentException("해당 음료는 주문에 없습니다.");
        }
        beverages.remove(beverage);
    }

    // clear : 주문 전체 삭제
    public void clear() {
        beverages.clear();
    }

    // calculateTotalPrice : 주문한 음료의 총 금액 계산
    public int calculateTotalPrice() {
        //stream으로 getPrice 가져와서 int로 변환, sum으로 합 계산
        return beverages.stream()
                .mapToInt(Beverage::getPrice)
                .sum();

        //stream 안쓴 버전-반복문으로 하나씩 가격가져와서 더함
//        int total = 0;
//        for (Beverage beverage : beverages) {
//            total += beverage.getPrice();
//        }
//        return total;

    }

    // createOrder : 주문 생성
    public Order createOrder() {

        return new Order(LocalDateTime.now(), beverages);
    }
}
