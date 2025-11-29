package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.learning.ProgressRequestDTO;
import com.example.E_Learning_CRUD.dto.learning.ProgressResponseDTO;
import com.example.E_Learning_CRUD.entity.Enrollment;
import com.example.E_Learning_CRUD.entity.Lecture;
import com.example.E_Learning_CRUD.entity.Progress;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.EnrollmentRepository;
import com.example.E_Learning_CRUD.repository.LectureRepository;
import com.example.E_Learning_CRUD.repository.ProgressRepository;
import com.example.E_Learning_CRUD.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;

    @Override
    public ProgressResponseDTO updateProgress(ProgressRequestDTO dto) {
        Enrollment enrollment = enrollmentRepository.findById(dto.getEnrollmentId()).orElseThrow(() -> new ResourceNotFoundException("Enrollment", dto.getEnrollmentId()));
        Lecture lecture = lectureRepository.findById(dto.getLectureId()).orElseThrow(() -> new ResourceNotFoundException("Lecture", dto.getLectureId()));
        // find existing progress or create new
        Progress p = progressRepository.findByEnrollmentIdAndLectureId(enrollment.getId(), lecture.getId())
                .orElseGet(() -> {
                    Progress newP = new Progress();
                    newP.setEnrollment(enrollment);
                    newP.setLecture(lecture);
                    return newP;
                });
        p.setProgressPercentage(dto.getProgressPercentage());
        p.setCompleted(dto.getProgressPercentage() >= 1.0);
        progressRepository.save(p);
        return toResponse(p);
    }

    @Override
    public List<ProgressResponseDTO> getByEnrollmentId(Long enrollmentId) {
        return progressRepository.findByEnrollmentId(enrollmentId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ProgressResponseDTO toResponse(Progress p) {
        ProgressResponseDTO r = new ProgressResponseDTO();
        r.setId(p.getId());
        r.setLectureId(p.getLecture().getId());
        r.setProgressPercentage(p.getProgressPercentage());
        r.setCompleted(p.getCompleted());
        return r;
    }
}
