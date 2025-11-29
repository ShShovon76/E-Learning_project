package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.QuestionRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuestionResponseDTO;
import com.example.E_Learning_CRUD.entity.Question;
import com.example.E_Learning_CRUD.entity.Quiz;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.QuestionRepository;
import com.example.E_Learning_CRUD.repository.QuizRepository;
import com.example.E_Learning_CRUD.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    @Override
    public QuestionResponseDTO create(QuestionRequestDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId()).orElseThrow(() -> new ResourceNotFoundException("Quiz", dto.getQuizId()));
        Question q = new Question();
        q.setQuiz(quiz);
        q.setQuestionText(dto.getQuestionText());
        q.setType(dto.getType());
        q.setPoints(dto.getPoints());
        questionRepository.save(q);
        return toResponse(q);
    }

    @Override
    public QuestionResponseDTO update(Long id, QuestionRequestDTO dto) {
        Question q = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question", id));
        q.setQuestionText(dto.getQuestionText());
        q.setType(dto.getType());
        q.setPoints(dto.getPoints());
        questionRepository.save(q);
        return toResponse(q);
    }

    @Override
    public QuestionResponseDTO getById(Long id) {
        return toResponse(questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question", id)));
    }

    @Override
    public List<QuestionResponseDTO> getByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!questionRepository.existsById(id)) throw new ResourceNotFoundException("Question", id);
        questionRepository.deleteById(id);
    }

    private QuestionResponseDTO toResponse(Question q) {
        QuestionResponseDTO r = new QuestionResponseDTO();
        r.setId(q.getId());
        r.setQuestionText(q.getQuestionText());
        r.setType(q.getType());
        return r;
    }
}
