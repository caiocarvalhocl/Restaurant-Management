package com.caio.restaurant.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.caio.restaurant.dto.request.CategoryRequest;
import com.caio.restaurant.dto.response.CategoryResponse;
import com.caio.restaurant.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Unchanged for backward compatibility: returns the full list, unpaginated.
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAll();
    }

    // Paginated sibling of getAllCategories(), added instead of changing the
    // existing endpoint's response shape so current callers keep working.
    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public Page<CategoryResponse> getCategoriesPaged(@PageableDefault(size = 20) Pageable pageable) {
        return categoryService.findAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public CategoryResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
