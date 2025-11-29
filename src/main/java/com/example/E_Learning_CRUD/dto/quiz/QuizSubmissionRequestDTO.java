package com.example.E_Learning_CRUD.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmissionRequestDTO {
    private Long studentId;
    private Long quizId;
}
