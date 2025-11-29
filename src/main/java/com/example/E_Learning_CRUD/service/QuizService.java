package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.QuizRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuizResponseDTO;

import java.util.List;

public interface QuizService {
    QuizResponseDTO create(QuizRequestDTO dto);
    QuizResponseDTO update(Long id, QuizRequestDTO dto);
    QuizResponseDTO getById(Long id);
    List<QuizResponseDTO> getByCourseId(Long courseId);
    void delete(Long id);
}
