package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_type in ('SELLING', 'HOLD');
     */

    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingTypes);

    Optional<Product> findByProductNumber(String productNumber);

    /**
     * select *
     * from product
     * where product_number in :productNumbers;
     */
    List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
