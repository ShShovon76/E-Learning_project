package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.learning.ProgressRequestDTO;
import com.example.E_Learning_CRUD.dto.learning.ProgressResponseDTO;

import java.util.List;

public interface ProgressService {
    ProgressResponseDTO updateProgress(ProgressRequestDTO dto);
    List<ProgressResponseDTO> getByEnrollmentId(Long enrollmentId);
}
