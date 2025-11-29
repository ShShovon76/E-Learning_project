package com.example.E_Learning_CRUD.dto.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponseDTO {
    private Long id;
    private String certificateId;
    private String studentName;
    private String courseName;
    private String certificateUrl;
    private LocalDateTime issueDate;
}
