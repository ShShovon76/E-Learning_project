package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.QuestionResponseRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuestionResponseResponseDTO;
import com.example.E_Learning_CRUD.entity.Question;
import com.example.E_Learning_CRUD.entity.QuestionResponse;
import com.example.E_Learning_CRUD.entity.QuizSubmission;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.QuestionRepository;
import com.example.E_Learning_CRUD.repository.QuestionResponseRepository;
import com.example.E_Learning_CRUD.repository.QuizSubmissionRepository;
import com.example.E_Learning_CRUD.service.QuestionResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionResponseServiceImpl implements QuestionResponseService {

    private final QuestionResponseRepository responseRepository;
    private final QuizSubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;

    @Override
    public QuestionResponseResponseDTO create(QuestionResponseRequestDTO dto) {
        QuizSubmission s = submissionRepository.findById(dto.getSubmissionId()).orElseThrow(() -> new ResourceNotFoundException("QuizSubmission", dto.getSubmissionId()));
        Question q = questionRepository.findById(dto.getQuestionId()).orElseThrow(() -> new ResourceNotFoundException("Question", dto.getQuestionId()));
        QuestionResponse qr = new QuestionResponse();
        qr.setSubmission(s);
        qr.setQuestion(q);
        qr.setAnswer(dto.getAnswer());
        // scoring logic omitted (auto-evaluate if MCQ)
        responseRepository.save(qr);
        QuestionResponseResponseDTO r = new QuestionResponseResponseDTO();
        r.setId(qr.getId());
        r.setCorrect(qr.getIsCorrect());
        r.setPointsEarned(qr.getPointsEarned());
        return r;
    }

    @Override
    public List<QuestionResponseResponseDTO> getBySubmissionId(Long submissionId) {
        return responseRepository.findBySubmissionId(submissionId).stream().map(qr -> {
            QuestionResponseResponseDTO r = new QuestionResponseResponseDTO();
            r.setId(qr.getId());
            r.setCorrect(qr.getIsCorrect());
            r.setPointsEarned(qr.getPointsEarned());
            return r;
        }).collect(Collectors.toList());
    }
}
