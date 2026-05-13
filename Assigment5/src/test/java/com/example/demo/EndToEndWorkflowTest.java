package com.example.demo;

import com.example.demo.dto.CreateOrderRequestDTO;
import com.example.demo.model.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EndToEndWorkflowTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCompleteOrderWorkflow() {
        // Step 1: Create a User
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");

        User savedUser = userService.addUser(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");

        // Step 2: Create Products
        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(1000.0);
        product1.setStockQuantity(10);
        product1.setDescription("High-performance laptop");

        Product product2 = new Product();
        product2.setName("Mouse");
        product2.setPrice(25.0);
        product2.setStockQuantity(100);
        product2.setDescription("Wireless mouse");

        Product savedProduct1 = productService.createProduct(product1);
        Product savedProduct2 = productService.createProduct(product2);

        assertThat(savedProduct1.getId()).isNotNull();
        assertThat(savedProduct2.getId()).isNotNull();

        // Verify initial stock
        Product verifyProduct1 = productService.getProductById(savedProduct1.getId());
        Product verifyProduct2 = productService.getProductById(savedProduct2.getId());
        assertThat(verifyProduct1.getStockQuantity()).isEqualTo(10);
        assertThat(verifyProduct2.getStockQuantity()).isEqualTo(100);

        // Step 3: Place an Order
        CreateOrderRequestDTO.OrderItemRequest item1 = new CreateOrderRequestDTO.OrderItemRequest();
        item1.setProductId(savedProduct1.getId());
        item1.setQuantity(2);

        CreateOrderRequestDTO.OrderItemRequest item2 = new CreateOrderRequestDTO.OrderItemRequest();
        item2.setProductId(savedProduct2.getId());
        item2.setQuantity(5);

        CreateOrderRequestDTO orderRequest = new CreateOrderRequestDTO();
        orderRequest.setUserId(savedUser.getId());
        orderRequest.setItems(List.of(item1, item2));

        Order placedOrder = orderService.placeOrder(orderRequest);

        // Step 4: Verify Order Details
        assertThat(placedOrder).isNotNull();
        assertThat(placedOrder.getId()).isNotNull();
        assertThat(placedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(placedOrder.getOrderItems()).hasSize(2);

        // Step 5: Verify Stock was Reduced
        Product product1AfterOrder = productService.getProductById(savedProduct1.getId());
        Product product2AfterOrder = productService.getProductById(savedProduct2.getId());

        assertThat(product1AfterOrder.getStockQuantity()).isEqualTo(8); // 10 - 2
        assertThat(product2AfterOrder.getStockQuantity()).isEqualTo(95); // 100 - 5

        // Step 6: Test Order Cancellation
        Order cancelledOrder = orderService.cancelOrder(placedOrder.getId());

        assertThat(cancelledOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        // Step 7: Verify Stock was Restored
        Product product1AfterCancel = productService.getProductById(savedProduct1.getId());
        Product product2AfterCancel = productService.getProductById(savedProduct2.getId());

        assertThat(product1AfterCancel.getStockQuantity()).isEqualTo(10); // 8 + 2 (restored)
        assertThat(product2AfterCancel.getStockQuantity()).isEqualTo(100); // 95 + 5 (restored)

        // Step 8: Verify User's Orders
        List<Order> userOrders = orderService.getUserOrders(savedUser.getId());
        assertThat(userOrders).hasSizeGreaterThanOrEqualTo(1);
        assertThat(userOrders).anyMatch(o -> o.getId().equals(cancelledOrder.getId()));
    }

    @Test
    void testMultipleUsersAndProductsWorkflow() {
        // Step 1: Create Multiple Users
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");

        User savedUser1 = userService.addUser(user1);
        User savedUser2 = userService.addUser(user2);

        // Step 2: Create Category and Products
        Product product = new Product();
        product.setName("Smartphone");
        product.setPrice(500.0);
        product.setStockQuantity(20);

        Product savedProduct = productService.createProduct(product);

        // Step 3: User 1 Places an Order
        CreateOrderRequestDTO.OrderItemRequest item1 = new CreateOrderRequestDTO.OrderItemRequest();
        item1.setProductId(savedProduct.getId());
        item1.setQuantity(2);

        CreateOrderRequestDTO order1Request = new CreateOrderRequestDTO();
        order1Request.setUserId(savedUser1.getId());
        order1Request.setItems(List.of(item1));

        Order order1 = orderService.placeOrder(order1Request);

        // Step 4: Verify stock after user 1's order
        Product afterOrder1 = productService.getProductById(savedProduct.getId());
        assertThat(afterOrder1.getStockQuantity()).isEqualTo(18); // 20 - 2

        // Step 5: User 2 Places an Order
        CreateOrderRequestDTO.OrderItemRequest item2 = new CreateOrderRequestDTO.OrderItemRequest();
        item2.setProductId(savedProduct.getId());
        item2.setQuantity(3);

        CreateOrderRequestDTO order2Request = new CreateOrderRequestDTO();
        order2Request.setUserId(savedUser2.getId());
        order2Request.setItems(List.of(item2));

        Order order2 = orderService.placeOrder(order2Request);

        // Step 6: Verify stock after user 2's order
        Product afterOrder2 = productService.getProductById(savedProduct.getId());
        assertThat(afterOrder2.getStockQuantity()).isEqualTo(15); // 18 - 3

        // Step 7: Verify both users have their orders
        List<Order> user1Orders = orderService.getUserOrders(savedUser1.getId());
        List<Order> user2Orders = orderService.getUserOrders(savedUser2.getId());

        assertThat(user1Orders).hasSizeGreaterThanOrEqualTo(1);
        assertThat(user2Orders).hasSizeGreaterThanOrEqualTo(1);
        assertThat(user1Orders.get(0).getId()).isEqualTo(order1.getId());
        assertThat(user2Orders.get(0).getId()).isEqualTo(order2.getId());

        // Step 8: Cancel user 1's order
        Order cancelledOrder1 = orderService.cancelOrder(order1.getId());

        // Step 9: Verify stock is restored for user 1's cancelled order
        Product afterCancellation = productService.getProductById(savedProduct.getId());
        assertThat(afterCancellation.getStockQuantity()).isEqualTo(17); // 15 + 2 (user 1's order restored)
    }
}
