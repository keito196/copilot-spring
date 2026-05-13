package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById_Found() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertThat(result).isNull();
        verify(productRepository).findById(1L);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        Product product = new Product();
        product.setName("New Product");

        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Product");
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        Product product = new Product();
        product.setName("Updated Product");

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = productService.updateProduct(1L, product);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Product");
        verify(productRepository).existsById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProduct_NotFound() {
        // Arrange
        Product product = new Product();

        when(productRepository.existsById(1L)).thenReturn(false);

        // Act
        Product result = productService.updateProduct(1L, product);

        // Assert
        assertThat(result).isNull();
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testSearchProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = List.of(new Product());
        Page<Product> page = new PageImpl<>(products, pageable, 1);

        when(productRepository.searchProducts("test", 1L, 10.0, 100.0, pageable)).thenReturn(page);

        // Act
        Page<Product> result = productService.searchProducts("test", 1L, 10.0, 100.0, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).searchProducts("test", 1L, 10.0, 100.0, pageable);
    }
}
