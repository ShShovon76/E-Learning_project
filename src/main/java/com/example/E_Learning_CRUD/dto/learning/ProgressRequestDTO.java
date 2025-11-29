package com.example.E_Learning_CRUD.dto.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressRequestDTO {
    private Long enrollmentId;
    private Long lectureId;
    private Double progressPercentage;
}
