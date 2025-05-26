package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    // 이전: 음료 1 개를 주문 목록에 추가할 수 있다.
    @DisplayName("음료 1개를 주문 목록에 추가하면 목록에 해당 음료가 포함된다.")
    @Test
    void add() {
        // given: 키오스크 생성
        CafeKiosk kiosk = new CafeKiosk();

        // when: 아메리카노 추가
        kiosk.add(new Americano());

        // then: 음료 리스트에 1개 추가되었는지 확인
        assertThat(kiosk.getBeverages()).hasSize(1);
        // 이름이 아메리카노인지, 가격이 4000 인지 확인
        assertThat(kiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(kiosk.getBeverages().get(0).getPrice()).isEqualTo(4000);
    }

    // 이전: 음료 0 개를 주문 목록에 추가할 수 없다.
    @DisplayName("음료 0개를 추가하면 예외가 발생한다.")
    @Test
    void addZeroBeverages() {
        // given: 키오스크 생성
        CafeKiosk kiosk = new CafeKiosk();

        // 0 개 추가할 경우 에러 발생 + "음료는 1잔 이상이어야 합니다." 메시지 출력
        assertThatThrownBy(() -> kiosk.addSeveralBeverages(new Americano(), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상이어야 합니다.");
    }

    // 이전: null 음료는 추가할 수 없다.
    @DisplayName("null 음료를 추가하면 예외가 발생한다.")
    @Test
    void add_nullBeverage() {
        // given : 키오스크 생성
        CafeKiosk kiosk = new CafeKiosk();

        // null 추가할 경우 에러 발생 + "음료는 null일 수 없습니다." 메시지 출력
        assertThatThrownBy(() -> kiosk.add(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 null일 수 없습니다.");
    }


    // 이전: 음료 여러 개를 주문 목록에 추가할 수 있다.
    @DisplayName("동일한 음료 여러 개를 추가하면 주문 목록에 모두 반영된다.")
    @Test
    void addSeveralBeverages() {
        // given: 키오스크 생성
        CafeKiosk kiosk = new CafeKiosk();

        // when: 라떼 3잔 추가
        kiosk.addSeveralBeverages(new Latte(), 3);

        // then: 리스트에 3개 추가되었는지 확인
        assertThat(kiosk.getBeverages()).hasSize(3);
        for(int i = 0; i < kiosk.getBeverages().size(); i++){
            // 추가된 모든 음료의 이름이 라떼인지, 가격이 4500 인지 확인
            assertThat(kiosk.getBeverages().get(i).getName()).isEqualTo("라뗴");
            assertThat(kiosk.getBeverages().get(i).getPrice()).isEqualTo(4500);
        }
    }

    // 이전: 음료 1 개를 주문 목록에서 제거할 수 있다.
    @DisplayName("주문 목록에서 음료 1개를 제거하면 목록에서 해당 음료가 사라진다.")
    @Test
    void remove() {
        // given: 라떼 추가
        CafeKiosk kiosk = new CafeKiosk();
        Latte latte = new Latte();
        kiosk.add(latte);

        // when: 라떼 제거
        kiosk.remove(latte);

        // then: 리스트가 비었는지 확인
        assertThat(kiosk.getBeverages()).isEmpty();
    }

    // 이전: 존재하지 않는 음료를 제거하려 해도 예외가 발생하지 않는다.
    @DisplayName("주문 목록에 없는 음료를 제거하려 하면 예외가 발생한다.")
    @Test
    void remove_notExistBeverage() {
        // given: 키오스크 생성
        CafeKiosk kiosk = new CafeKiosk();

        // then:
        assertThatThrownBy(() -> kiosk.remove(new Americano()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제하려는 음료가 주문 목록에 존재하지 않습니다.");
    }


    // 이전: 주문 목록을 초기화할 수 있다.
    @DisplayName("주문 목록을 초기화하면 모든 음료가 제거된다.")
    @Test
    void clear() {
        // given: 아메리카노, 라떼 추가
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when: 전체 초기화
        kiosk.clear();

        // then: 리스트가 비었는지 확인
        assertThat(kiosk.getBeverages()).isEmpty();
    }

    // 이전: 주문한 모든 음료의 총 금액을 계산할 수 있다.
    @DisplayName("주문한 음료들의 총 금액을 정확히 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given: 아메리카노(4000), 라떼(4500) 추가
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when: 총 금액 계산
        int totalPrice = kiosk.calculateTotalPrice();
        int testPrice = 4500 + 4000;
        // then: 합계가 8500인지 확인
        assertThat(totalPrice).isEqualTo(testPrice);
    }

    // 이전: 주문을 생성하면 주문 목록과 주문 시간이 포함된다.
    @DisplayName("주문을 생성하면 주문 목록과 생성 시간이 포함된다.")
    @Test
    void createOrder() {
        // given: 음료 2개 추가
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when: 주문 생성
        Order order = kiosk.createOrder();

        // then: 음료 리스트와 주문 시간 확인
        assertThat(order.getBeverages()).hasSize(2);
        assertThat(order.getOrderDateTime()).isNotNull();
    }

    // 이전: 주문 생성 시 현재 시간 기준으로 주문 시간이 설정된다.
    @DisplayName("주문 시간은 주문 생성 시점 기준으로 정확하게 설정된다.")
    @Test
    void createOrder_time() {
        // given: 음료 2개 추가
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());
        kiosk.add(new Latte());

        // when: 경계값들(현재 시간)과 주문 생성
        LocalDateTime before = LocalDateTime.now();
        Order order = kiosk.createOrder();
        LocalDateTime after = LocalDateTime.now();

        // then: 현재 시간에서 1 나노초 뺀 값과 1 나노초 더한 값에 대해 주문 시간이 각각 이후인지, 이전인지 테스트
        // 현재 시간에서 1 나노초 뺀 값: 현재 시간에서 1 초 더 과거이므로 주문 시간이 이 값보다 이후여야 함
        assertThat(order.getOrderDateTime()).isAfter(before.minusNanos(1));
        // 현재 시간에서 1 나노초 더한 값: 현재 시간에서 1 초 더 이후이므로 주문 시간이 이 값보다 이전여야 함
        assertThat(order.getOrderDateTime()).isBefore(after.plusNanos(1));
    }
}