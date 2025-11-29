package com.example.E_Learning_CRUD.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {
    private String name;
    private String description;
    private Long parentCategoryId;

}
