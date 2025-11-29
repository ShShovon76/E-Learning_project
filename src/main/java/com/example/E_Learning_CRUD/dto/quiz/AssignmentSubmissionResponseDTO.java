package com.example.E_Learning_CRUD.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentSubmissionResponseDTO {
    private Long id;
    private Integer grade;
    private String feedback;
}
