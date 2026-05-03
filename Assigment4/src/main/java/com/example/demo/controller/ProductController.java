package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Get all products
     * @return List of all products
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return Product details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(convertToDTO(product));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Create a new product
     * @param productDTO Product data
     * @return Created product
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdProduct));
    }

    /**
     * Update an existing product
     * @param id Product ID
     * @param productDTO Updated product data
     * @return Updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a product
     * @param id Product ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Advanced search for products with filtering and pagination
     * Supports filtering by keyword (name/description), category, and price range
     * 
     * @param keyword Search keyword in product name or description
     * @param categoryId Category ID to filter by
     * @param minPrice Minimum price filter
     * @param maxPrice Maximum price filter
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return Page of products matching the criteria
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProducts(keyword, categoryId, minPrice, maxPrice, pageable);
        Page<ProductDTO> productDTOPage = productPage.map(this::convertToDTO);
        
        return ResponseEntity.ok(productDTOPage);
    }

    /**
     * Convert Product entity to ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }
        return dto;
    }

    /**
     * Convert ProductDTO to Product entity
     */
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }
}

