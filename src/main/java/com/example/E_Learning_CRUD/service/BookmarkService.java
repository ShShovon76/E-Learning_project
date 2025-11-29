package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.learning.BookmarkRequestDTO;
import com.example.E_Learning_CRUD.dto.learning.BookmarkResponseDTO;

import java.util.List;

public interface BookmarkService {
    BookmarkResponseDTO create(BookmarkRequestDTO dto);
    List<BookmarkResponseDTO> getByStudentId(Long studentId);
    void delete(Long id);
}
