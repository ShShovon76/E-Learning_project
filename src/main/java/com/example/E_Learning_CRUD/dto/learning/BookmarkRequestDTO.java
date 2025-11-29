package com.example.E_Learning_CRUD.dto.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRequestDTO {
    private Long studentId;
    private Long lectureId;
    private String note;
}
