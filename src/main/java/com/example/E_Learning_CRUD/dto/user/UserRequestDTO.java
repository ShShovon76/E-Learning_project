package com.example.E_Learning_CRUD.dto.user;

import com.example.E_Learning_CRUD.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String profilePicture;
    private String bio;
    private Role role;
}
