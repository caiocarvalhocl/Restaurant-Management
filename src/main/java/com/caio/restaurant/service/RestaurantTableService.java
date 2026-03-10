package com.caio.restaurant.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.caio.restaurant.dto.request.RestaurantTableRequest;
import com.caio.restaurant.dto.response.RestaurantTableResponse;
import com.caio.restaurant.exception.ResourceNotFoundException;
import com.caio.restaurant.repository.RestaurantTableRepository;

@Service
public class RestaurantTableService {

        private final RestaurantTableRepository restaurantTableRepository;

        public RestaurantTableService(RestaurantTableRepository restaurantTableRepository) {
                this.restaurantTableRepository = restaurantTableRepository;
        }

        @Transactional(readOnly = true)
        public List<RestaurantTableResponse> findAll() {
                return restaurantTableRepository.findAll().stream()
                                .map(RestaurantTableResponse::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public RestaurantTableResponse findById(Long id) {
                return restaurantTableRepository.findById(id)
                                .map(RestaurantTableResponse::toResponse)
                                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        }

        @Transactional
        public RestaurantTableResponse create(RestaurantTableRequest request) {
                var entity = restaurantTableRepository.save(RestaurantTableResponse.toEntity(request));
                return RestaurantTableResponse.toResponse(entity);
        }

        @Transactional
        public RestaurantTableResponse update(Long id, RestaurantTableRequest request) {
                var existingTable = restaurantTableRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

                existingTable.setTableNumber(request.tableNumber());
                existingTable.setCapacity(request.capacity());
                if (request.status() != null) {
                        existingTable.setStatus(request.status());
                }

                return RestaurantTableResponse.toResponse(restaurantTableRepository.save(existingTable));
        }

        @Transactional
        public void delete(Long id) {
                if (!restaurantTableRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Table not found with id: " + id);
                }
                restaurantTableRepository.deleteById(id);
        }
}
