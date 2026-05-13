package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Integer quantity;
    private Double price;
    private Long orderId;
    private Long productId;
}
