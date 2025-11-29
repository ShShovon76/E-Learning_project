package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.course.SectionRequestDTO;
import com.example.E_Learning_CRUD.dto.course.SectionResponseDTO;

import java.util.List;

public interface SectionService {
    SectionResponseDTO create(SectionRequestDTO dto);
    SectionResponseDTO update(Long id, SectionRequestDTO dto);
    SectionResponseDTO getById(Long id);
    List<SectionResponseDTO> getByCourseId(Long courseId);
    void delete(Long id);
}
