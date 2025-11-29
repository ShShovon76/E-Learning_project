package com.example.E_Learning_CRUD.service;

import com.example.E_Learning_CRUD.dto.course.LectureRequestDTO;
import com.example.E_Learning_CRUD.dto.course.LectureResponseDTO;

import java.util.List;

public interface LectureService {
    LectureResponseDTO create(LectureRequestDTO dto);
    LectureResponseDTO update(Long id, LectureRequestDTO dto);
    LectureResponseDTO getById(Long id);
    List<LectureResponseDTO> getBySectionId(Long sectionId);
    void delete(Long id);
}
