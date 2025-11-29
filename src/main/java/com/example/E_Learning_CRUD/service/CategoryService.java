package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.course.CategoryRequestDTO;
import com.example.E_Learning_CRUD.dto.course.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO create(CategoryRequestDTO dto);
    CategoryResponseDTO update(Long id, CategoryRequestDTO dto);
    CategoryResponseDTO getById(Long id);
    List<CategoryResponseDTO> getAll();
    void delete(Long id);
}
