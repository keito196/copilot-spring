package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private Long userId;
    private Set<OrderItemDTO> orderItems;
}
