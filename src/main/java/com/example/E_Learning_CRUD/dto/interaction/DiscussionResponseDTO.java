package com.example.E_Learning_CRUD.dto.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionResponseDTO {
    private Long id;
    private String title;
    private String content;
}
