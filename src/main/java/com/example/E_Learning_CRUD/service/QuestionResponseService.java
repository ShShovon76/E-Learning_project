package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.QuestionResponseRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuestionResponseResponseDTO;

import java.util.List;

public interface QuestionResponseService {
    QuestionResponseResponseDTO create(QuestionResponseRequestDTO dto);
    List<QuestionResponseResponseDTO> getBySubmissionId(Long submissionId);
}
