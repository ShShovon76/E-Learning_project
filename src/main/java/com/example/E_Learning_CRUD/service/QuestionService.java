package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.QuestionRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuestionResponseDTO;

import java.util.List;

public interface QuestionService {
    QuestionResponseDTO create(QuestionRequestDTO dto);
    QuestionResponseDTO update(Long id, QuestionRequestDTO dto);
    QuestionResponseDTO getById(Long id);
    List<QuestionResponseDTO> getByQuizId(Long quizId);
    void delete(Long id);
}
