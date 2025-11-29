package com.example.E_Learning_CRUD.dto.course;

import com.example.E_Learning_CRUD.enums.LectureType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureRequestDTO {
    private Long sectionId;
    private String title;
    private String description;
    private LectureType type;
    private String contentUrl;
    private String thumbnailUrl;
    private Long durationSeconds;
    private Boolean preview;
    private Integer orderIndex;
}
