package com.example.demo;

import com.example.demo.dto.CreateOrderRequestDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void testGetAllOrders() throws Exception {
        // Arrange
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.PENDING);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.CONFIRMED);

        when(orderService.getAllOrders()).thenReturn(List.of(order1, order2));

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"));
    }

    @Test
    void testGetOrderById_Found() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        when(orderService.getOrderById(1L)).thenReturn(order);

        // Act & Assert
        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        // Arrange
        when(orderService.getOrderById(anyLong())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/orders/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPlaceOrder() throws Exception {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setItems(List.of());

        when(orderService.placeOrder(any(CreateOrderRequestDTO.class))).thenReturn(order);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        // Act
        doNothing().when(orderService).deleteOrder(1L);

        // Assert
        mockMvc.perform(delete("/api/orders/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
