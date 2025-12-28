package com.caio.restaurant.dto.response;

import com.caio.restaurant.dto.request.CategoryRequest;
import com.caio.restaurant.entity.Category;

public record CategoryResponse(Long id, String name, String description) {
  public static CategoryResponse toResponse(Category category) {
    return new CategoryResponse(
        category.getId(),
        category.getName(),
        category.getDescription());
  }

  public static Category toEntity(CategoryRequest request) {
    Category category = new Category();
    category.setName(request.name());
    category.setDescription(request.description());
    return category;
  }
}
