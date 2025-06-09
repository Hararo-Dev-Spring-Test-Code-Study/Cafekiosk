package sample.cafekiosk.spring.api.dto.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private List<String> productNumbers;
}
