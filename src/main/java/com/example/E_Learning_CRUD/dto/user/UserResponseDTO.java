package com.example.E_Learning_CRUD.dto.user;

import com.example.E_Learning_CRUD.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String profilePicture;
    private Role role;
    private LocalDateTime createdAt;
}
