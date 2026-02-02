package com.caio.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.caio.restaurant.dto.request.RestaurantTableRequest;
import com.caio.restaurant.dto.response.RestaurantTableResponse;
import com.caio.restaurant.repository.RestaurantTableRepository;

import jakarta.transaction.Transactional;

@Service
public class RestaurantTableService {
        private final RestaurantTableRepository restaurantTableRepository;

        public RestaurantTableService(RestaurantTableRepository restaurantTableRepository) {
                this.restaurantTableRepository = restaurantTableRepository;
        }

        @Transactional
        public List<RestaurantTableResponse> findAll() {
                return restaurantTableRepository.findAll().stream().map(RestaurantTableResponse::toResponse).toList();
        }

        @Transactional
        public RestaurantTableResponse findById(Long id) {
                return restaurantTableRepository.findById(id).map(RestaurantTableResponse::toResponse).orElse(null);
        }

        @Transactional
        public RestaurantTableResponse create(RestaurantTableRequest request) {
                var entity = restaurantTableRepository.save(RestaurantTableResponse.toEntity(request));
                return RestaurantTableResponse.toResponse(entity);
        }

        @Transactional
        public RestaurantTableResponse update(Long id, RestaurantTableRequest request) {
                return restaurantTableRepository.findById(id).map(existingTable -> {
                        var updatedTable = RestaurantTableResponse.toEntity(request);
                        updatedTable.setId(existingTable.getId());
                        var savedTable = restaurantTableRepository.save(updatedTable);
                        return RestaurantTableResponse.toResponse(savedTable);
                }).orElse(null);
        }

        @Transactional
        public void delete(Long id) {
                restaurantTableRepository.deleteById(id);
        }

}
