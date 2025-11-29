package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    // Find progress by enrollmentId and lectureId
    Optional<Progress> findByEnrollmentIdAndLectureId(Long enrollmentId, Long lectureId);

    // Find all progress records for an enrollment
    List<Progress> findByEnrollmentId(Long enrollmentId);
}
