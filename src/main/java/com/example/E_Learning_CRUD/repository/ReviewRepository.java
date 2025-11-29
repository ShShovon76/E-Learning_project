package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
