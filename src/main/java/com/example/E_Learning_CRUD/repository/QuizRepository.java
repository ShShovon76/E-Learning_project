package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz>findByCourseId(Long courseId);
}
