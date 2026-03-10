package com.caio.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.caio.restaurant.entity.BillItem;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}
