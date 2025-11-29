package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.AnswerRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.AnswerResponseDTO;
import com.example.E_Learning_CRUD.entity.Answer;
import com.example.E_Learning_CRUD.entity.Question;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.AnswerRepository;
import com.example.E_Learning_CRUD.repository.QuestionRepository;
import com.example.E_Learning_CRUD.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public AnswerResponseDTO create(AnswerRequestDTO dto) {
        Question q = questionRepository.findById(dto.getQuestionId()).orElseThrow(() -> new ResourceNotFoundException("Question", dto.getQuestionId()));
        Answer a = new Answer();
        a.setQuestion(q);
        a.setAnswerText(dto.getAnswerText());
        a.setIsCorrect(dto.getCorrect());
        answerRepository.save(a);
        return toResponse(a);
    }

    @Override
    public AnswerResponseDTO update(Long id, AnswerRequestDTO dto) {
        Answer a = answerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer", id));
        a.setAnswerText(dto.getAnswerText());
        a.setIsCorrect(dto.getCorrect());
        answerRepository.save(a);
        return toResponse(a);
    }

    @Override
    public List<AnswerResponseDTO> getByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!answerRepository.existsById(id)) throw new ResourceNotFoundException("Answer", id);
        answerRepository.deleteById(id);
    }

    private AnswerResponseDTO toResponse(Answer a) {
        AnswerResponseDTO r = new AnswerResponseDTO();
        r.setId(a.getId());
        r.setAnswerText(a.getAnswerText());
        r.setCorrect(a.getIsCorrect());
        return r;
    }
}
