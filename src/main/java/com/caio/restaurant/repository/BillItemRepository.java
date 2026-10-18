package com.caio.restaurant.repository;

import com.caio.restaurant.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {}
