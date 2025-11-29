package com.example.E_Learning_CRUD.service.impl;


import com.example.E_Learning_CRUD.dto.learning.CertificateResponseDTO;
import com.example.E_Learning_CRUD.entity.Certificate;
import com.example.E_Learning_CRUD.entity.Enrollment;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CertificateRepository;
import com.example.E_Learning_CRUD.repository.EnrollmentRepository;
import com.example.E_Learning_CRUD.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public CertificateResponseDTO generateForEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new ResourceNotFoundException("Enrollment", enrollmentId));
        Certificate c = new Certificate();
        c.setCertificateId(UUID.randomUUID().toString());
        c.setEnrollment(enrollment);
        c.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        c.setCourseName(enrollment.getCourse().getTitle());
        c.setInstructorName(enrollment.getCourse().getInstructor() != null ? (enrollment.getCourse().getInstructor().getFirstName()+" "+enrollment.getCourse().getInstructor().getLastName()) : null);
        c.setIssueDate(LocalDateTime.now());
        // set certificateUrl after generating PDF in background or via another service
        certificateRepository.save(c);
        CertificateResponseDTO dto = new CertificateResponseDTO();
        dto.setId(c.getId());
        dto.setCertificateId(c.getCertificateId());
        dto.setStudentName(c.getStudentName());
        dto.setCourseName(c.getCourseName());
        dto.setCertificateUrl(c.getCertificateUrl());
        dto.setIssueDate(c.getIssueDate());
        return dto;
    }

    @Override
    public Optional<CertificateResponseDTO> getById(Long id) {
        return certificateRepository.findById(id).map(c -> {
            CertificateResponseDTO dto = new CertificateResponseDTO();
            dto.setId(c.getId());
            dto.setCertificateId(c.getCertificateId());
            dto.setStudentName(c.getStudentName());
            dto.setCourseName(c.getCourseName());
            dto.setCertificateUrl(c.getCertificateUrl());
            dto.setIssueDate(c.getIssueDate());
            return dto;
        });
    }
}
