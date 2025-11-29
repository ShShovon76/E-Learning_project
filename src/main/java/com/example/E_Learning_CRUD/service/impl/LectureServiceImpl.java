package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.course.LectureRequestDTO;
import com.example.E_Learning_CRUD.dto.course.LectureResponseDTO;
import com.example.E_Learning_CRUD.entity.Lecture;
import com.example.E_Learning_CRUD.entity.Section;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.LectureRepository;
import com.example.E_Learning_CRUD.repository.SectionRepository;
import com.example.E_Learning_CRUD.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final SectionRepository sectionRepository;

    @Override
    public LectureResponseDTO create(LectureRequestDTO dto) {
        Section section = sectionRepository.findById(dto.getSectionId()).orElseThrow(() -> new ResourceNotFoundException("Section", dto.getSectionId()));
        Lecture l = new Lecture();
        l.setSection(section);
        l.setTitle(dto.getTitle());
        l.setDescription(dto.getDescription());
        l.setType(dto.getType());
        l.setContentUrl(dto.getContentUrl());
        l.setThumbnailUrl(dto.getThumbnailUrl());
        l.setDurationSeconds(dto.getDurationSeconds());
        l.setPreview(dto.getPreview());
        l.setOrderIndex(dto.getOrderIndex());
        lectureRepository.save(l);
        return toResponse(l);
    }

    @Override
    public LectureResponseDTO update(Long id, LectureRequestDTO dto) {
        Lecture l = lectureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lecture", id));
        l.setTitle(dto.getTitle());
        l.setDescription(dto.getDescription());
        l.setType(dto.getType());
        l.setContentUrl(dto.getContentUrl());
        l.setThumbnailUrl(dto.getThumbnailUrl());
        l.setDurationSeconds(dto.getDurationSeconds());
        l.setPreview(dto.getPreview());
        l.setOrderIndex(dto.getOrderIndex());
        lectureRepository.save(l);
        return toResponse(l);
    }

    @Override
    public LectureResponseDTO getById(Long id) {
        return toResponse(lectureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lecture", id)));
    }

    @Override
    public List<LectureResponseDTO> getBySectionId(Long sectionId) {
        return lectureRepository.findBySectionIdOrderByOrderIndex(sectionId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!lectureRepository.existsById(id)) throw new ResourceNotFoundException("Lecture", id);
        lectureRepository.deleteById(id);
    }

    private LectureResponseDTO toResponse(Lecture l) {
        LectureResponseDTO r = new LectureResponseDTO();
        r.setId(l.getId());
        r.setTitle(l.getTitle());
        r.setType(l.getType());
        r.setDurationSeconds(l.getDurationSeconds());
        r.setPreview(l.getPreview());
        return r;
    }
}
