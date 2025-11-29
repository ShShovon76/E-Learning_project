package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Question;
import com.example.E_Learning_CRUD.entity.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {
    List<QuestionResponse> findBySubmissionId(Long submissionId);

}
