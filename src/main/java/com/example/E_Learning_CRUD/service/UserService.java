package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.user.UserRequestDTO;
import com.example.E_Learning_CRUD.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO create(UserRequestDTO dto);
    UserResponseDTO update(Long id, UserRequestDTO dto);
    UserResponseDTO getById(Long id);
    List<UserResponseDTO> getAll();
    void delete(Long id);
}
