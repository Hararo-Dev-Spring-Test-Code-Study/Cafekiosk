package sample.cafekiosk.unit;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.cafekiosk.unit.beverage.Beverage;

import java.util.List;

@Getter
public class CafeKiosk {
    private static final Logger log = LoggerFactory.getLogger(CafeKiosk.class);

    // add : 음료 1잔 추가
    public void add(Beverage beverage) {
        String beverageName = beverage.getName();
        int beveragePrice = beverage.getPrice();

        log.info("Adding {} with price {}", beverageName, beveragePrice);
    }

    // addSeveralBeverages : 음료 여러 잔 추가
    public void addSeveralBeverages(List<Beverage> beverages) {
        
    }

    // remove : 음료 1잔 주문 삭제
    // clear : 주문 전체 삭제
    // calculateTotalPrice : 주문한 음료의 총 금액 계산
    // createOrder : 주문 생성

}
