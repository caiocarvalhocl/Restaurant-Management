package com.caio.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caio.restaurant.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
