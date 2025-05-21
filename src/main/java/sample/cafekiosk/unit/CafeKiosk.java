package sample.cafekiosk.unit;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {
    private static final LocalTime OPEN_TIME = LocalTime.of(10, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);
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
    public Order createOrder(LocalDateTime orderDateTime) {
        //LocalDateTime과 LocalTime을 비교하려고 할 때 발생하는 타입 불일치 오류가 발생하므로 toLocalTime()으로 변환해줘야함
        LocalTime orderTime = orderDateTime.toLocalTime();
        if (orderTime.isBefore(OPEN_TIME) || orderTime.isAfter(CLOSE_TIME)) {
            throw new IllegalArgumentException("주문 가능한 시간이 아닙니다. 영업시간은 10:00~22:00입니다.");
        }
        return new Order(orderDateTime, beverages);
    }
}
