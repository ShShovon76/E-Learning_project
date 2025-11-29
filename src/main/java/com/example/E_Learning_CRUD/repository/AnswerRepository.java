package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer>findByQuestionId(Long questionId);
}
