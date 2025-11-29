package com.example.E_Learning_CRUD.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponseDTO {
    private Long id;
    private String title;
    private Integer orderIndex;
    private List<LectureResponseDTO> lectures;
}
