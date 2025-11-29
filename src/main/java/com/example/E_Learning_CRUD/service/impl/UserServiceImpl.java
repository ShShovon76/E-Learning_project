package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.user.UserRequestDTO;
import com.example.E_Learning_CRUD.dto.user.UserResponseDTO;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        User u = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword()) // hash upstream or here with PasswordEncoder
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .profilePicture(dto.getProfilePicture())
                .bio(dto.getBio())
                .role(dto.getRole())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(u);
        return toResponse(u);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhone(dto.getPhone());
        u.setProfilePicture(dto.getProfilePicture());
        u.setBio(dto.getBio());
        u.setRole(dto.getRole());
        u.setUpdatedAt(LocalDateTime.now());
        userRepository.save(u);
        return toResponse(u);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        return toResponse(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!userRepository.existsById(id)) throw new ResourceNotFoundException("User", id);
        userRepository.deleteById(id);
    }

    private UserResponseDTO toResponse(User u) {
        UserResponseDTO r = new UserResponseDTO();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setEmail(u.getEmail());
        r.setFullName((u.getFirstName()==null?"":u.getFirstName()) + " " + (u.getLastName()==null?"":u.getLastName()));
        r.setProfilePicture(u.getProfilePicture());
        r.setRole(u.getRole());
        r.setCreatedAt(u.getCreatedAt());
        return r;
    }
}
