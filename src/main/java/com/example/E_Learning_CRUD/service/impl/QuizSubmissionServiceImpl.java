package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.QuizSubmissionRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuizSubmissionResponseDTO;
import com.example.E_Learning_CRUD.entity.Quiz;
import com.example.E_Learning_CRUD.entity.QuizSubmission;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.enums.SubmissionStatus;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.QuizRepository;
import com.example.E_Learning_CRUD.repository.QuizSubmissionRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.QuizSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizSubmissionServiceImpl implements QuizSubmissionService {

    private final QuizSubmissionRepository submissionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    @Override
    public QuizSubmissionResponseDTO submit(QuizSubmissionRequestDTO dto) {
        User student = userRepository.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getStudentId()));
        Quiz quiz = quizRepository.findById(dto.getQuizId()).orElseThrow(() -> new ResourceNotFoundException("Quiz", dto.getQuizId()));
        QuizSubmission s = new QuizSubmission();
        s.setStudent(student);
        s.setQuiz(quiz);
        s.setStartedAt(LocalDateTime.now());
        s.setStatus(SubmissionStatus.SUBMITTED);
        submissionRepository.save(s);
        QuizSubmissionResponseDTO r = new QuizSubmissionResponseDTO();
        r.setId(s.getId());
        r.setQuizId(quiz.getId());
        r.setScore(s.getScore());
        return r;
    }

    @Override
    public QuizSubmissionResponseDTO grade(Long submissionId) {
        QuizSubmission s = submissionRepository.findById(submissionId).orElseThrow(() -> new ResourceNotFoundException("QuizSubmission", submissionId));
        // grading logic omitted here - implement auto-grade or manual grading
        s.setStatus(SubmissionStatus.GRADED);
        s.setSubmittedAt(LocalDateTime.now());
        submissionRepository.save(s);
        QuizSubmissionResponseDTO r = new QuizSubmissionResponseDTO();
        r.setId(s.getId());
        r.setQuizId(s.getQuiz().getId());
        r.setScore(s.getScore());
        return r;
    }

    @Override
    public List<QuizSubmissionResponseDTO> getByStudentId(Long studentId) {
        return submissionRepository.findByStudentId(studentId).stream().map(s -> {
            QuizSubmissionResponseDTO r = new QuizSubmissionResponseDTO();
            r.setId(s.getId());
            r.setQuizId(s.getQuiz().getId());
            r.setScore(s.getScore());
            return r;
        }).collect(Collectors.toList());
    }
}

