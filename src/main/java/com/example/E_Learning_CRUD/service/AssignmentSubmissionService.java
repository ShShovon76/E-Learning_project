package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.quiz.AssignmentSubmissionRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.AssignmentSubmissionResponseDTO;

import java.util.List;

public interface AssignmentSubmissionService {
    AssignmentSubmissionResponseDTO submit(AssignmentSubmissionRequestDTO dto);
    AssignmentSubmissionResponseDTO grade(Long submissionId, Integer grade, String feedback);
    List<AssignmentSubmissionResponseDTO> getByAssignmentId(Long assignmentId);
}
