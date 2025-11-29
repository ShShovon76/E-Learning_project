package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.learning.EnrollmentRequestDTO;
import com.example.E_Learning_CRUD.dto.learning.EnrollmentResponseDTO;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponseDTO enroll(EnrollmentRequestDTO dto);
    EnrollmentResponseDTO getById(Long id);
    List<EnrollmentResponseDTO> getByStudentId(Long studentId);
    void completeEnrollment(Long id);
    void delete(Long id);
}
