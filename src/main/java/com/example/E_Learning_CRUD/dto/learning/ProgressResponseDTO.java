package com.example.E_Learning_CRUD.dto.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResponseDTO {
    private Long id;
    private Long lectureId;
    private Double progressPercentage;
    private Boolean completed;
}
