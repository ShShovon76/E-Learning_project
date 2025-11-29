package com.example.E_Learning_CRUD.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDTO {
    private Long id;
    private String answerText;
    private Boolean correct;
}
