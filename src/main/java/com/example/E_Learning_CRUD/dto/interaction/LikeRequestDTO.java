package com.example.E_Learning_CRUD.dto.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequestDTO {
    private Long userId;
    private Long discussionId;
    private Long commentId;
}
