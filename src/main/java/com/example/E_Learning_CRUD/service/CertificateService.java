package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.learning.CertificateResponseDTO;

import java.util.Optional;

public interface CertificateService {
    CertificateResponseDTO generateForEnrollment(Long enrollmentId);
    Optional<CertificateResponseDTO> getById(Long id);
}
