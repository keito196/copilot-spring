package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequestDTO {
    private Long userId;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
