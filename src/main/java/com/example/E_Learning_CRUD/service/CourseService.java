package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.course.CourseRequestDTO;
import com.example.E_Learning_CRUD.dto.course.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    CourseResponseDTO create(CourseRequestDTO dto);
    CourseResponseDTO update(Long id, CourseRequestDTO dto);
    CourseResponseDTO getById(Long id);
    List<CourseResponseDTO> getAll();
    void delete(Long id);
}
