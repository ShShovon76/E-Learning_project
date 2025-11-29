package com.example.E_Learning_CRUD.dto.interaction;

import com.example.E_Learning_CRUD.enums.DiscussionType;
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
    private DiscussionType type;
}
