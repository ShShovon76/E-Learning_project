package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark>findByStudentId(Long studentId);
}
