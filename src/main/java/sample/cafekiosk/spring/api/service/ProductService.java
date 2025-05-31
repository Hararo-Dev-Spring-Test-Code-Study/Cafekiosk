package sample.cafekiosk.spring.api.service;

import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.dto.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getSellingProducts() {
        return productRepository.findAllBySellingStatus(ProductSellingStatus.SELLING)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
