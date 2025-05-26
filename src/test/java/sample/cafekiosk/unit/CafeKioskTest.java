package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

// assertThat(AssertJ)
// assertThat(actual).isEqualsTo(expected);
// 더 읽기 쉽고 다양한 조건, 메서드 체이닝 -> 단위 테스트에서 거의 표준
// assertEquals(Junit)
// assertEquals(expected, actual);
// 확장성이 제한적 -> 보조적으로 사용
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//JUnit 테스트 메서드는 반드시 void 리턴형이고 매개변수가 없어야 함
class CafeKioskTest {

//    @DisplayName("음료 1개 추가하면 음료 리스트에 해당 음료가 포함된다")
    @DisplayName("음료 한 개를 추가하면 해당 음료가 주문 목록에 추가된다.")
    @Test
    void addOneBeverage() {
        // given
        CafeKiosk kiosk = new CafeKiosk();

        // when
        kiosk.add(new Americano());

        // then
        assertThat(kiosk.getBeverages().size()).isEqualTo(1);
        assertThat(kiosk.getBeverages().get(0).getClass()).isEqualTo(Americano.class);
    }

//    @DisplayName("여러 개의 음료를 추가하면 모든 음료가 순서대로 주문 음료 리스트에 포함된다")
    @DisplayName("여러 개의 음료를 추가하면 여러 잔의 음료가 순서대로 주문 목록에 추가된다.")
    @Test
//    void addSeveralBeverages() {
    void addSeveralBeverages() {
        // given
        CafeKiosk kiosk = new CafeKiosk();

        // when
        kiosk.add(new Americano(), 3);
        kiosk.add(new Latte(), 2);

        // then
        assertThat(kiosk.getBeverages().size()).isEqualTo(5);
        assertThat(kiosk.getBeverages())
                // 각 객체의 "class" 속성을 추출
                .extracting("class")
                // 포함 여부만 테스트
                .contains(Americano.class, Latte.class)
                // 순서까지 정확히 일치하는지 검증
                .containsExactly(Americano.class, Americano.class, Americano.class,
                                Latte.class, Latte.class);
    }

//    @DisplayName("음료를 한 개 추가한 후 제거하면 주문 음료 리스트에서 해당 음료가 삭제된다")
    @DisplayName("주문 목록에 담긴 음료 한 개를 제거하면 해당 음료가 주문 목록에서 삭제된다.")
    @Test
    void remove() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        Americano americano = new Americano();
        kiosk.add(americano);

        // when
        kiosk.remove(americano);

        // then
        assertThat(kiosk.getBeverages()).isEmpty();
    }

//    @DisplayName("여러 개의 음료를 추가한 후 clear를 호출하면 음료 리스트가 비워진다")
    @DisplayName("주문 목록 전체를 비우는 기능을 사용하면 모든 음료들이 주문 목록에서 삭제된다.")
    @Test
    void clear() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when
        kiosk.clear();

        // then
        assertThat(kiosk.getBeverages()).isEmpty();
    }

//    @DisplayName("주문 음료들의 가격 합계를 정확히 계산한다")
    @DisplayName("주문 목록에 담긴 음료의 총 가격을 정확하게 계산할 수 있다.")
    @Test
    void calculateTotalPrice() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when
        int totalPrice = kiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(8500);
    }

//    @DisplayName("주문 생성 시 현재 주문 음료 리스트를 기준으로 주문이 만들어진다")
    @DisplayName("주문 생성 기능을 사용하면 현재 주문 목록에 담긴 음료들의 순서대로 주문이 생성된다.")
    @Test
    void createOrder() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when
        Order order = kiosk.createOrder();

        // then
        assertThat(order.getBeverages()).hasSize(2);
        assertThat(order.getBeverages())
                .extracting("class")
                .containsExactly(Americano.class, Latte.class);
    }

//    @DisplayName("주문 생성 시 생성 요청 시점의 시간이 주문에 기록된다")
    @DisplayName("주문이 생성될 때 주문 시각은 해당 주문이 생성된 시점으로 기록된다.")
    @Test
    void createOrderTime() {
        // given
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        LocalDateTime before = LocalDateTime.now();
        System.out.println("Before Order: " + before);

        // when
        Order order = kiosk.createOrder();
        System.out.println("Order Time: " + order.getOrderDateTime());

        LocalDateTime after = LocalDateTime.now();
        System.out.println("After Order: " + after);

        // then
        assertThat(order.getOrderDateTime()).isAfterOrEqualTo(before);
        assertThat(order.getOrderDateTime()).isBeforeOrEqualTo(after);
    }
}


//assertThatThrownBy() .isInstanceOf() .hasMessage()

//OrderDateTime() 테스트 할 때 코드 실행 시점 테스트 하기 어려워 LocalDateTime 을 외부로 빼서 주문 생성 메서드 작성 -> 테스트 용이

//LocalDateTime.of(year: 2025, month:3, dayOfMonth:3, hour:10, minute;0)); 으로 직접 설정하고 테스트 진행
//경계값 테스트로 진행