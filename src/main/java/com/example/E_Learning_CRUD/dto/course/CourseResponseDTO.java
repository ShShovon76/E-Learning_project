package com.example.E_Learning_CRUD.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String subtitle;
    private String instructorName;
    private String categoryName;
    private BigDecimal price;
    private Double averageRating;
    private LocalDateTime createdAt;

}
