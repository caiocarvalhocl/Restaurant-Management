package com.caio.restaurant.repository;

import com.caio.restaurant.entity.Bill;
import com.caio.restaurant.enums.BillStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

  List<Bill> findByStatus(BillStatus status);

  List<Bill> findByTableId(Long tableId);
}
