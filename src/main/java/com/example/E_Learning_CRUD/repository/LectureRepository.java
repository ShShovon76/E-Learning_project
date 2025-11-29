package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Lecture;
import com.example.E_Learning_CRUD.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findBySectionIdOrderByOrderIndex(Long sectionId);
}
