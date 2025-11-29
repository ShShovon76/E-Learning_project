package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.interaction.ReviewRequestDTO;
import com.example.E_Learning_CRUD.dto.interaction.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO create(ReviewRequestDTO dto);
    List<ReviewResponseDTO> getByCourseId(Long courseId);
    void delete(Long id);
}
