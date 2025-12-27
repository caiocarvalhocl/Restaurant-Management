package com.caio.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.caio.restaurant.entity.Product;
import com.caio.restaurant.exception.ResourceNotFoundException;
import com.caio.restaurant.repository.ProductRepository;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + String.valueOf(id)));
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(Long id, Product productDetails) {
        Product productSearched = this.findById(id);

        productSearched.setName(productDetails.getName());
        productSearched.setDescription(productDetails.getDescription());
        productSearched.setPrice(productDetails.getPrice());
        productSearched.setPriceType(productDetails.getPriceType());
        productSearched.setAvailable(productDetails.getAvailable());
        productSearched.setCategory(productDetails.getCategory());

        return productRepository.save(productSearched);
    }

    public void delete(Long id) {
        Product productSearched = this.findById(id);
        productRepository.delete(productSearched);
    }

}
