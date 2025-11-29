package com.example.E_Learning_CRUD.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentSubmissionRequestDTO {
    private Long assignmentId;
    private Long studentId;
    private String fileUrl;
    private String textSubmission;
}
