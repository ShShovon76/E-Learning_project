package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.interaction.DiscussionRequestDTO;
import com.example.E_Learning_CRUD.dto.interaction.DiscussionResponseDTO;

import java.util.List;

public interface DiscussionService {
    DiscussionResponseDTO create(DiscussionRequestDTO dto);
    List<DiscussionResponseDTO> getByCourseId(Long courseId);
    void delete(Long id);
}
