package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.AssignmentSubmissionRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.AssignmentSubmissionResponseDTO;
import com.example.E_Learning_CRUD.entity.Assignment;
import com.example.E_Learning_CRUD.entity.AssignmentSubmission;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.AssignmentRepository;
import com.example.E_Learning_CRUD.repository.AssignmentSubmissionRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.AssignmentSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private final AssignmentSubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Override
    public AssignmentSubmissionResponseDTO submit(AssignmentSubmissionRequestDTO dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new ResourceNotFoundException("Assignment", dto.getAssignmentId()));
        User student = userRepository.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getStudentId()));
        AssignmentSubmission s = new AssignmentSubmission();
        s.setAssignment(assignment);
        s.setStudent(student);
        s.setFileUrl(dto.getFileUrl());
        s.setTextSubmission(dto.getTextSubmission());
        s.setSubmittedAt(LocalDateTime.now());
        submissionRepository.save(s);
        AssignmentSubmissionResponseDTO r = new AssignmentSubmissionResponseDTO();
        r.setId(s.getId());
        r.setGrade(s.getGrade());
        r.setFeedback(s.getFeedback());
        return r;
    }

    @Override
    public AssignmentSubmissionResponseDTO grade(Long submissionId, Integer grade, String feedback) {
        AssignmentSubmission s = submissionRepository.findById(submissionId).orElseThrow(() -> new ResourceNotFoundException("AssignmentSubmission", submissionId));
        s.setGrade(grade);
        s.setFeedback(feedback);
        s.setGradedAt(LocalDateTime.now());
        submissionRepository.save(s);
        AssignmentSubmissionResponseDTO r = new AssignmentSubmissionResponseDTO();
        r.setId(s.getId());
        r.setGrade(s.getGrade());
        r.setFeedback(s.getFeedback());
        return r;
    }

    @Override
    public List<AssignmentSubmissionResponseDTO> getByAssignmentId(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId).stream().map(s -> {
            AssignmentSubmissionResponseDTO r = new AssignmentSubmissionResponseDTO();
            r.setId(s.getId());
            r.setGrade(s.getGrade());
            r.setFeedback(s.getFeedback());
            return r;
        }).collect(Collectors.toList());
    }
}
