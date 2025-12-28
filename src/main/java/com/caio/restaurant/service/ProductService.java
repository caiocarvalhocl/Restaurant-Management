package com.caio.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.caio.restaurant.dto.response.ProductResponse;
import com.caio.restaurant.dto.request.ProductRequest;
import com.caio.restaurant.entity.Product;
import com.caio.restaurant.entity.Category;
import com.caio.restaurant.exception.ResourceNotFoundException;
import com.caio.restaurant.repository.ProductRepository;
import com.caio.restaurant.repository.CategoryRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(ProductResponse::toResponse).toList();
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.toResponse(this.findProductById(id));
    }

    public ProductResponse create(ProductRequest request) {
        Category category = this.findCategoryById(request.categoryId());
        Product product = ProductResponse.toEntity(request);

        product.setCategory(category);

        return ProductResponse.toResponse(productRepository.save(product));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = this.findProductById(id);

        Category category = this.findCategoryById(request.categoryId());

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setPriceType(request.priceType());
        product.setAvailable(request.available());
        product.setCategory(category);

        return ProductResponse.toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.delete(findProductById(id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not found!"));
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

}
