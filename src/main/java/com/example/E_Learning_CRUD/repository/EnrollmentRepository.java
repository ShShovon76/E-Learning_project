package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    List<Enrollment> findByStudentId(Long studentId);
}
