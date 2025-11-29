package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
