package com.example.E_Learning_CRUD.dto.course;

import com.example.E_Learning_CRUD.enums.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
    private String title;
    private String subtitle;
    private String description;
    private String learningObjectives;
    private String requirements;
    private String targetAudience;
    private Long instructorId;
    private Long categoryId;
    private CourseLevel level;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String thumbnailUrl;
    private String promoVideoUrl;
}
