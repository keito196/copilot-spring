package com.example.demo;

import com.example.demo.dto.CreateOrderRequestDTO;
import com.example.demo.model.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCancelOrder_Success() {
        // Arrange: Create a user and a PENDING order
        User user = new User();
        user.setUsername("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStockQuantity(100);
        product = productRepository.save(product);

        CreateOrderRequestDTO.OrderItemRequest itemRequest = new CreateOrderRequestDTO.OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(10);

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(user.getId());
        request.setItems(List.of(itemRequest));

        Order order = orderService.placeOrder(request);
        Long orderId = order.getId();

        // Verify initial stock
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(90); // 100 - 10

        // Act: Call cancelOrder
        orderService.cancelOrder(orderId);

        // Assert: Verify order status is CANCELLED and stock restored
        Order cancelledOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(cancelledOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        Product restoredProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(restoredProduct.getStockQuantity()).isEqualTo(100); // restored to 100
    }

    @Test
    void testCancelOrder_Failure_OrderNotFound() {
        // Act & Assert: Try to cancel a non-existent order
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(999L);
        });
        assertThat(exception.getMessage()).isEqualTo("Order not found");
    }

    @Test
    void testCancelOrder_Failure_OrderNotCancellable() {
        // Arrange: Create a user and a SHIPPED order
        User user = new User();
        user.setUsername("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setStockQuantity(100);
        product = productRepository.save(product);

        CreateOrderRequestDTO.OrderItemRequest itemRequest = new CreateOrderRequestDTO.OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(10);

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(user.getId());
        request.setItems(List.of(itemRequest));

        Order order = orderService.placeOrder(request);
        order.setStatus(OrderStatus.SHIPPED); // Manually set to SHIPPED
        orderRepository.save(order);
        Long orderId = order.getId();

        // Act & Assert: Try to cancel a SHIPPED order
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(orderId);
        });
        assertThat(exception.getMessage()).isEqualTo("Order cannot be cancelled. Current status: SHIPPED");
    }
}
