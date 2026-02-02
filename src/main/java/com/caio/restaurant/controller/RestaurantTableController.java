package com.caio.restaurant.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.restaurant.dto.request.RestaurantTableRequest;
import com.caio.restaurant.dto.response.RestaurantTableResponse;
import com.caio.restaurant.service.RestaurantTableService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/tables")
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<RestaurantTableResponse> getAllTables() {
        return restaurantTableService.findAll();
    }

    @GetMapping("/:id")
    @PreAuthorize("isAuthenticated()")
    public RestaurantTableResponse getTableById(@PathVariable Long id) {
        return restaurantTableService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public RestaurantTableResponse createTable(@RequestBody RestaurantTableRequest request) {
        return restaurantTableService.create(request);
    }

    @DeleteMapping("/:id")
    @PreAuthorize("hasRole('OWNER')")
    public void deleteTable(@PathVariable Long id) {
        restaurantTableService.delete(id);
    }
}
