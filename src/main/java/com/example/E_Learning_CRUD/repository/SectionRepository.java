package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseIdOrderByOrderIndex(Long courseId);
}
