package com.example.E_Learning_CRUD.dto.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemLogResponseDTO {
    private Long id;
    private String action;
    private String description;
    private String ipAddress;
    private LocalDateTime timestamp;
}
