package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.interaction.CommentRequestDTO;
import com.example.E_Learning_CRUD.dto.interaction.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO create(CommentRequestDTO dto);
    List<CommentResponseDTO> getByDiscussionId(Long discussionId);
    void delete(Long id);
}
