package com.example.E_Learning_CRUD.dto.course;

import com.example.E_Learning_CRUD.enums.LectureType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureResponseDTO {
    private Long id;
    private String title;
    private LectureType type;
    private Long durationSeconds;
    private Boolean preview;
}
