package com.caio.restaurant.dto.response;

import com.caio.restaurant.dto.request.ProductRequest;
import com.caio.restaurant.entity.Product;
import com.caio.restaurant.dto.response.CategorySummaryResponse;

import java.math.BigDecimal;

import com.caio.restaurant.enums.PriceType;

public record ProductResponse(Long id, String name, String description, BigDecimal price, PriceType priceType,
    Boolean available, CategorySummaryResponse category) {
  public static ProductResponse toResponse(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getPriceType(),
        product.getAvailable(),
        new CategorySummaryResponse(product.getCategory().getId(), product.getCategory().getName()));
  }

  public static Product toEntity(ProductRequest productRequest) {
    Product product = new Product();
    product.setName(productRequest.name());
    product.setDescription(productRequest.description());
    product.setPrice(productRequest.price());
    product.setPriceType(productRequest.priceType());
    product.setAvailable(productRequest.available());
    return product;
  }
}
