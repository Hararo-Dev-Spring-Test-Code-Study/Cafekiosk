package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_type in ('SELLING', 'HOLD');
     */

    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    // native 쿼리로 데이터를 날림
    // id기준으로 역순 정렬했을 때, 가장 상위에 있는 상품번호를 하나 뽑아와라
    @Query(value = "select p.product_number from product p order by id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();

}
