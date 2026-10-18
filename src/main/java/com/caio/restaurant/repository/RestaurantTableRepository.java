package com.caio.restaurant.repository;

import com.caio.restaurant.entity.RestaurantTable;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
  Optional<RestaurantTable> findByTableNumber(int tableNumber);
}
