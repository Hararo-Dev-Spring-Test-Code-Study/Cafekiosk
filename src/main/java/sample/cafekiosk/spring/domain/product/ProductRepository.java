// Repository Layer : DB에 실제로 접근하는 DAO(Data Access Object)
// Product 테이블에 쿼리를 날리는 역할(select, insert 등)
// Product 엔티티를 저장, 조회, 수정, 삭제
// JPA를 통해 DB와의 연결을 추상화함
// save(), findAll(), findById() 등 제공됨

package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// week 8 추가
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
// <Product 엔티티, PK Long>
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_status in ('SELLING', 'HOLD');
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    // week 8 추가
    // Product 테이블에서 최근 저장된 상품(마지막 저장 상품)의 상품번호를 native SQL로 조회
    @Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();
}
