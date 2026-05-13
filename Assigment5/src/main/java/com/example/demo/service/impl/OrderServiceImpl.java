package com.example.demo.service.impl;

import com.example.demo.dto.CreateOrderRequestDTO;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Order placeOrder(CreateOrderRequestDTO request) {
        // Retrieve the user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and save a new Order entity
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setOrderItems(new HashSet<>());

        // Validate stock and process each item
        for (CreateOrderRequestDTO.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Check if there is enough stock in the Product table
            if (product.getStockQuantity() == null || product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName() +
                        ". Available: " + (product.getStockQuantity() != null ? product.getStockQuantity() : 0) +
                        ", Requested: " + itemRequest.getQuantity()
                );
            }

            // Decrease the stockQuantity of the Product and save it
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            // Create and save a new OrderItem entity with the product details and price snapshot
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice()); // Price snapshot
            orderItem.setProduct(product);
            orderItem.setOrder(order);

            orderItemRepository.save(orderItem);

            // Link all OrderItems to the Order
            order.getOrderItems().add(orderItem);
        }

        // Save the final order with all items linked
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        if (orderRepository.existsById(id)) {
            order.setId(id);
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if the order can be cancelled (e.g., only PENDING orders)
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled. Current status: " + order.getStatus());
        }

        // Change status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);

        // Restore stock for each OrderItem
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // Save and return the updated order
        return orderRepository.save(order);
    }
}
