package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
