package com.caio.restaurant.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.caio.restaurant.entity.Category;
import com.caio.restaurant.exception.ResourceNotFoundException;
import com.caio.restaurant.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = this.findById(id);
        categoryRepository.delete(category);
    }

    public Category update(Long id, Category categoryDetails) {
        Category category = this.findById(id);

        System.out.println(category);
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        System.out.println(category);

        return categoryRepository.save(category);
    }
}
