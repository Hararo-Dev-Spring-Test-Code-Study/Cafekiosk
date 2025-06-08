package sample.cafekiosk.spring.api.controller.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private List<String> productNumbers;
}