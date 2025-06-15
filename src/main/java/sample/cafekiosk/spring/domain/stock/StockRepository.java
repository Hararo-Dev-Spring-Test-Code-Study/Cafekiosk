// Week 7 추가

package sample.cafekiosk.spring.domain.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 해당 인터페이스가 데이터 접근 계층(DAO:Data Access Object)임을 나타냄(생략해도 Spring이 인식)
// Spring Data JPA가 런타임에 인터페이스->구현체 자동으로 생성
@Repository
// JpaRepository : Spring Data JPA가 제공하는 인터페이스로 기본 CRUD 제공
// JpaRepository<T, ID>
// T : 엔티티 클래스(Stock)
// ID : 엔티티의 PK 타입(Long)
// JpaRepository 상속받으면
// save(), findById(), findAll(), deletedById(), count() 등의 메서드를
// 직접 구현하지 않아도 사용 가능
public interface StockRepository extends JpaRepository<Stock, Long> {

    // 커스텀 쿼리 메서드
    // 메서드 이름으로 Spring이 SQL 쿼리 자동 생성
    // SELECT * FROM stock WHERE product_number IN (productNumber) 와 유사한 쿼리가 내부적으로 실행됨
    List<Stock> findAllByProductNumberIn(List<String> productNumbers);

}
