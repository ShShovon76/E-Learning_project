package com.example.E_Learning_CRUD.dto.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponseDTO {
    private Long id;
    private Long courseId;
    private Integer enrollments;
    private Integer completions;
    private BigDecimal revenue;
}
