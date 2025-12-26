package com.caio.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caio.restaurant.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
