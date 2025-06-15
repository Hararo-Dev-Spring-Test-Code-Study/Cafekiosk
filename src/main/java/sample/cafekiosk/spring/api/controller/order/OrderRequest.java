package sample.cafekiosk.spring.api.controller.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Map<String, Integer> productNumberWithQuantity;
}