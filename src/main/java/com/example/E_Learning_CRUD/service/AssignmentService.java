package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.AssignmentRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.AssignmentResponseDTO;

import java.util.List;

public interface AssignmentService {
    AssignmentResponseDTO create(AssignmentRequestDTO dto);
    AssignmentResponseDTO update(Long id, AssignmentRequestDTO dto);
    AssignmentResponseDTO getById(Long id);
    List<AssignmentResponseDTO> getByCourseId(Long courseId);
    void delete(Long id);
}
