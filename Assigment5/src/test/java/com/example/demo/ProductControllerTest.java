package com.example.demo;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void testGetAllProducts() throws Exception {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(200.0);

        when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(150.0);
        product.setStockQuantity(50);

        when(productService.getProductById(1L)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.stockQuantity").value(50));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        // Arrange
        when(productService.getProductById(anyLong())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("New Product");
        product.setPrice(99.99);
        product.setStockQuantity(100);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        productDTO.setPrice(99.99);
        productDTO.setStockQuantity(100);

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");
        product.setPrice(150.0);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(150.0);

        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void testUpdateProduct_NotFound() throws Exception {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");

        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);

        when(productService.getProductById(1L)).thenReturn(product);
        doNothing().when(productService).deleteProduct(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        // Arrange
        when(productService.getProductById(anyLong())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
