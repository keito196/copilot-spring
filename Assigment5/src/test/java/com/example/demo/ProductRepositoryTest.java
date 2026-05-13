package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindAll() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(100.0);
        product1.setStockQuantity(50);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(200.0);
        product2.setStockQuantity(30);

        productRepository.save(product1);
        productRepository.save(product2);

        // Act
        var products = productRepository.findAll();

        // Assert
        assertThat(products).hasSize(2);
    }

    @Test
    void testFindById() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(150.0);
        productRepository.save(product);

        // Act
        Optional<Product> found = productRepository.findById(product.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Product");
    }

    @Test
    void testSaveProduct() {
        // Arrange
        Product product = new Product();
        product.setName("New Product");
        product.setPrice(99.99);
        product.setStockQuantity(100);

        // Act
        Product savedProduct = productRepository.save(product);

        // Assert
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("New Product");
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Product to Delete");
        Product saved = productRepository.save(product);
        Long productId = saved.getId();

        // Act
        productRepository.deleteById(productId);

        // Assert
        Optional<Product> deleted = productRepository.findById(productId);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testSearchProducts_ByKeyword() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Laptop Computer");
        product1.setDescription("High-performance laptop");
        product1.setPrice(1000.0);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Mouse");
        product2.setDescription("Wireless mouse");
        product2.setPrice(25.0);
        productRepository.save(product2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Product> result = productRepository.searchProducts("laptop", null, null, null, pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Laptop Computer");
    }

    @Test
    void testSearchProducts_ByPriceRange() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Budget Product");
        product1.setPrice(50.0);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Premium Product");
        product2.setPrice(500.0);
        productRepository.save(product2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Product> result = productRepository.searchProducts(null, null, 100.0, 600.0, pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Premium Product");
    }

    @Test
    void testSearchProducts_NoResults() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        productRepository.save(product);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Product> result = productRepository.searchProducts("nonexistent", null, null, null, pageable);

        // Assert
        assertThat(result.getContent()).isEmpty();
    }
}
