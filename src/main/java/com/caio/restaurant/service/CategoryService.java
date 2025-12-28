package com.caio.restaurant.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.caio.restaurant.dto.request.CategoryRequest;
import com.caio.restaurant.dto.response.CategoryResponse;
import com.caio.restaurant.entity.Category;
import com.caio.restaurant.exception.ResourceNotFoundException;
import com.caio.restaurant.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(CategoryResponse::toResponse).toList();
    }

    public CategoryResponse findById(Long id) {
        return CategoryResponse.toResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!")));
    }

    public CategoryResponse create(CategoryRequest categoryRequest) {
        Category category = CategoryResponse.toEntity(categoryRequest);
        return CategoryResponse.toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        category.setName(categoryDetails.name());
        category.setDescription(categoryDetails.description());

        return CategoryResponse.toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        categoryRepository.delete(category);
    }
}
