// Week 7 추가

package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

// Spring Boot에서 JPA 관련 컴포넌트만 로드하는 테스트 전용 어노테이션
// Repository만 테스트 할 수 있도록 @Entity, @Repository 등만 메모리에 올림
// 기본적으로 인메모리 DB(H2 등)를 사용해 실제 DB 건드리지 않고 테스트
// 트랜잭션을 자동으로 걸고 테스트가 끝나면 롤백해줘 안전함
@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);
        // 3개의 stock 객체를 DB에 저장
        // Stock 테이블의 productNumber 속성과 quantity 속성에 각각 정보가 저장된 형태
        // product_number   quantity
        // "001"            1
        // "002"            2
        // "003"            3
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // when
        // 쿼리 메서드로 productNumber 가 "001" 또는 "002" 인 데이터를 가져옴
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        // stocks가 "001", "002" 두개만 가져왔는지
        assertThat(stocks).hasSize(2)
                // 가져온 stocks 데이터에서 productNumber, quantity 속성만 꺼냄
                .extracting("productNumber", "quantity")
                // 순서는 상관없이 "001" 1개, "002" 2개인지 확인
                .containsExactlyInAnyOrder(
                        tuple("001", 1),
                        tuple("002", 2)
                );
    }

}