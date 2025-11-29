package com.example.E_Learning_CRUD.dto.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionRequestDTO {
    private Long courseId;
    private Long studentId;
    private String title;
    private String content;
    private String type;
}
