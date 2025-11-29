package com.example.E_Learning_CRUD.dto.quiz;

import com.example.E_Learning_CRUD.enums.QuizType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizRequestDTO {
    private Long courseId;
    private String title;
    private QuizType type;
    private Integer timeLimitMinutes;
    private Double passingScore;
}
