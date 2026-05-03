package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequestDTO;
import com.example.demo.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order placeOrder(CreateOrderRequestDTO request);
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
    List<Order> getUserOrders(Long userId);
}
