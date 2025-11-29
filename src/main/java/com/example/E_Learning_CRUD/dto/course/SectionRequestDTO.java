package com.example.E_Learning_CRUD.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionRequestDTO {
    private Long courseId;
    private String title;
    private String description;
    private Integer orderIndex;
}
