package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.QuizRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.QuizResponseDTO;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.entity.Quiz;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.repository.QuizRepository;
import com.example.E_Learning_CRUD.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;

    @Override
    public QuizResponseDTO create(QuizRequestDTO dto) {
        Quiz q = new Quiz();
        if(dto.getCourseId()!=null) {
            Course c = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course", dto.getCourseId()));
            q.setCourse(c);
        }
        q.setTitle(dto.getTitle());
        q.setType(dto.getType());
        q.setTimeLimitMinutes(dto.getTimeLimitMinutes());
        q.setPassingScore(dto.getPassingScore());
        quizRepository.save(q);
        return toResponse(q);
    }

    @Override
    public QuizResponseDTO update(Long id, QuizRequestDTO dto) {
        Quiz q = quizRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", id));
        q.setTitle(dto.getTitle());
        q.setType(dto.getType());
        q.setTimeLimitMinutes(dto.getTimeLimitMinutes());
        q.setPassingScore(dto.getPassingScore());
        quizRepository.save(q);
        return toResponse(q);
    }

    @Override
    public QuizResponseDTO getById(Long id) {
        return toResponse(quizRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", id)));
    }

    @Override
    public List<QuizResponseDTO> getByCourseId(Long courseId) {
        return quizRepository.findByCourseId(courseId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!quizRepository.existsById(id)) throw new ResourceNotFoundException("Quiz", id);
        quizRepository.deleteById(id);
    }

    private QuizResponseDTO toResponse(Quiz q) {
        QuizResponseDTO r = new QuizResponseDTO();
        r.setId(q.getId());
        r.setTitle(q.getTitle());
        r.setTimeLimitMinutes(q.getTimeLimitMinutes());
        r.setPassingScore(q.getPassingScore());
        return r;
    }
}
