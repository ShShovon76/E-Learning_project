package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.AnswerRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.AnswerResponseDTO;

import java.util.List;

public interface AnswerService {
    AnswerResponseDTO create(AnswerRequestDTO dto);
    AnswerResponseDTO update(Long id, AnswerRequestDTO dto);
    List<AnswerResponseDTO> getByQuestionId(Long questionId);
    void delete(Long id);
}
