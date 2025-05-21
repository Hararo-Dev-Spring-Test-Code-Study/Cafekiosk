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
    private static final Logger log = LoggerFactory.getLogger(CafeKiosk.class);
    // 매장 오픈 시간 : 10시 / 매장 마감 시간 : 22시
    private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0, 0);
    private static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0, 0);

    private final List<Beverage> beverages = new ArrayList<>();

    // add : 음료 1잔 추가
    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    // addSeveralBeverages : 음료 여러 잔 추가
    public void addSeveralBeverages(Beverage beverage, int count) {
        if(count <= 0) {
            throw new IllegalArgumentException("음료는 1잔 이상 주문해야합니다.");
        } else {
            for (int i = 0; i < count; i++) {
                beverages.add(beverage);
            }
        }
    }

    // remove : 음료 1잔 주문 삭제
    public void remove(Beverage beverage) {
        beverages.remove(beverage);
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

        // int totalPrice = 0;
        //    for (Beverage beverage : beverages) {
        //        totalPrice += beverage.getPrice();
        //    }
        //    return totalPrice;
        // 리스트 요소를 순회하며 금액을 계산하는걸 stream함수를 써서 계산
    }

    // createOrder : 주문 생성
    public Order createOrder(LocalDateTime localDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");
        }

        return new Order(currentDateTime, beverages);
    }

}
