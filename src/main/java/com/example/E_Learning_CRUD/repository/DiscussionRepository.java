package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Discussion;
import com.example.E_Learning_CRUD.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByCourseId(Long courseId);
}
