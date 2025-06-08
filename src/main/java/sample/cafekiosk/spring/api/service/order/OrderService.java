package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.order.*;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor // 똑같다 !
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

//    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository) {
//        this.orderRepository = orderRepository;
//        this.orderProductRepository = orderProductRepository;
//        this.productRepository = productRepository;
//    }

    public Order createOrder(List<String> productNumbers) {
        // 초기 : PENDING(대기 중) 으로 주문 객체 생성
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .build();

        // OrderProduct 를 만들기 위해 Order 의 ID 가 필요하기 때문에 Order 객체를 미리 저장
        orderRepository.save(order);

        // productNumbers 를 사용해 Product List 를 찾아옴
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        if (products.size() != productNumbers.size()) {
            throw new IllegalArgumentException("상품 번호 중 일치하는 상품이 없는 번호가 있습니다.");
        }


        // 조회된 Product 각각에 대해 OrderProduct 생성
        // 하나의 주문에 여러 개의 상품이 연결됨
        List<OrderProduct> orderProducts = products.stream()
                .map(product ->
                        OrderProduct
                                .builder()
                                .order(order)
                                .product(product)
                                .build()
                )
                .toList();

        // 위에서 생성한 OrderProduct 목록을 DB에 한 번에 저장
        orderProductRepository.saveAll(orderProducts);

        // 저장된 Order 를 반환
        return order;
    }


    public int getTotalPrice(Order order) {
        // order 를 사용해 Product 리스트를 찾아옴.
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

        // 위에서 찾아온 각각의 Product 에서 price 를 뽑아냄
        // int 인 속성을 계산해야 하므로 mapToInt 사용
        // sum 을 통해 뽑은 가격들을 전부 더함.
        return orderProducts
                .stream()
                .mapToInt(orderProduct ->
                        orderProduct.getProduct().getPrice()
                )
                .sum();
    }
}
