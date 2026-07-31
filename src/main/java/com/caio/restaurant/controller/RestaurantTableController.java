package com.caio.restaurant.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.restaurant.dto.request.RestaurantTableRequest;
import com.caio.restaurant.dto.response.RestaurantTableResponse;
import com.caio.restaurant.service.RestaurantTableService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tables")
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    // Unchanged for backward compatibility: returns the full list, unpaginated.
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<RestaurantTableResponse> getAllTables() {
        return restaurantTableService.findAll();
    }

    // Paginated sibling of getAllTables(), added instead of changing the
    // existing endpoint's response shape so current callers keep working.
    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public Page<RestaurantTableResponse> getTablesPaged(@PageableDefault(size = 20) Pageable pageable) {
        return restaurantTableService.findAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public RestaurantTableResponse getTableById(@PathVariable Long id) {
        return restaurantTableService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public RestaurantTableResponse createTable(@Valid @RequestBody RestaurantTableRequest request) {
        return restaurantTableService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public RestaurantTableResponse updateTable(@PathVariable Long id, @Valid @RequestBody RestaurantTableRequest request) {
        return restaurantTableService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public void deleteTable(@PathVariable Long id) {
        restaurantTableService.delete(id);
    }
}
