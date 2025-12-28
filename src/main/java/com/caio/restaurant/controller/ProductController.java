package com.caio.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.restaurant.service.ProductService;
import com.caio.restaurant.dto.response.ProductResponse;
import com.caio.restaurant.dto.request.ProductRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> findAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productDetails) {
        return productService.update(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
