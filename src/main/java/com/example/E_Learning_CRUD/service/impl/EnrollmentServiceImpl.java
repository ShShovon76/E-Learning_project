package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.learning.EnrollmentRequestDTO;
import com.example.E_Learning_CRUD.dto.learning.EnrollmentResponseDTO;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.entity.Enrollment;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.enums.EnrollmentStatus;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.repository.EnrollmentRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public EnrollmentResponseDTO enroll(EnrollmentRequestDTO dto) {
        User student = userRepository.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course", dto.getCourseId()));
        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        e.setEnrolledAt(LocalDateTime.now());
        e.setStatus(EnrollmentStatus.ACTIVE);
        // paidAmount/transactionId managed by Order/Payment flow
        enrollmentRepository.save(e);
        return toResponse(e);
    }

    @Override
    public EnrollmentResponseDTO getById(Long id) {
        return toResponse(enrollmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Enrollment", id)));
    }

    @Override
    public List<EnrollmentResponseDTO> getByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void completeEnrollment(Long id) {
        Enrollment e = enrollmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
        e.setStatus(EnrollmentStatus.COMPLETED);
        e.setCompletedAt(LocalDateTime.now());
        enrollmentRepository.save(e);
    }

    @Override
    public void delete(Long id) {
        if(!enrollmentRepository.existsById(id)) throw new ResourceNotFoundException("Enrollment", id);
        enrollmentRepository.deleteById(id);
    }

    private EnrollmentResponseDTO toResponse(Enrollment e) {
        EnrollmentResponseDTO r = new EnrollmentResponseDTO();
        r.setId(e.getId());
        r.setStudentId(e.getStudent().getId());
        r.setCourseId(e.getCourse().getId());
        r.setStatus(e.getStatus());
        r.setEnrolledAt(e.getEnrolledAt());
        return r;
    }
}
