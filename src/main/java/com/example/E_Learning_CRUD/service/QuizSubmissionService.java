package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.QuizSubmissionRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuizSubmissionResponseDTO;

import java.util.List;

public interface QuizSubmissionService {
    QuizSubmissionResponseDTO submit(QuizSubmissionRequestDTO dto);
    QuizSubmissionResponseDTO grade(Long submissionId);
    List<QuizSubmissionResponseDTO> getByStudentId(Long studentId);
}
