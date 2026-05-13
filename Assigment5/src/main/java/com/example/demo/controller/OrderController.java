package com.example.demo.controller;

import com.example.demo.dto.CreateOrderRequestDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.service.OrderService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all orders
     * @return List of all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    /**
     * Get order by ID
     * @param id Order ID
     * @return Order details
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(convertToDTO(order));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get order history for a specific user
     * @param userId User ID
     * @return List of orders for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    /**
     * Place a new order
     * This endpoint accepts a request containing user ID and order items
     * It will:
     * 1. Validate stock availability for all items
     * 2. Create an Order with PENDING status
     * 3. Create OrderItems linked to the Order
     * 4. Reduce product stock quantities
     * 
     * @param createOrderRequest Request containing userId and items to order
     * @return Created order with all details
     */
    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody CreateOrderRequestDTO createOrderRequest) {
        try {
            Order createdOrder = orderService.placeOrder(createOrderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdOrder));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            // Handle InsufficientStockException or other exceptions
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Update order status
     * @param id Order ID
     * @param order Updated order data
     * @return Updated order
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO order) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder != null) {
            Order updatedOrder = orderService.updateOrder(id, existingOrder);
            return ResponseEntity.ok(convertToDTO(updatedOrder));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete an order
     * @param id Order ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Convert Order entity to OrderDTO
     */
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus() != null ? order.getStatus().toString() : null);
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }
        
        // Convert OrderItems to OrderItemDTOs
        if (order.getOrderItems() != null) {
            Set<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                    .map(this::convertOrderItemToDTO)
                    .collect(Collectors.toSet());
            dto.setOrderItems(orderItemDTOs);
        }
        
        return dto;
    }

    /**
     * Convert OrderItem entity to OrderItemDTO
     */
    private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        if (orderItem.getOrder() != null) {
            dto.setOrderId(orderItem.getOrder().getId());
        }
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
        }
        return dto;
    }
}

