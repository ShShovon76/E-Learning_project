package com.example.E_Learning_CRUD.dto.quiz;

import com.example.E_Learning_CRUD.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDTO {
    private Long quizId;
    private String questionText;
    private QuestionType type;
    private Integer points;
}
