package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.course.SectionRequestDTO;
import com.example.E_Learning_CRUD.dto.course.SectionResponseDTO;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.entity.Section;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.repository.SectionRepository;
import com.example.E_Learning_CRUD.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    @Override
    public SectionResponseDTO create(SectionRequestDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course", dto.getCourseId()));
        Section s = new Section();
        s.setCourse(course);
        s.setTitle(dto.getTitle());
        s.setDescription(dto.getDescription());
        s.setOrderIndex(dto.getOrderIndex());
        sectionRepository.save(s);
        return toResponse(s);
    }

    @Override
    public SectionResponseDTO update(Long id, SectionRequestDTO dto) {
        Section s = sectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section", id));
        s.setTitle(dto.getTitle());
        s.setDescription(dto.getDescription());
        s.setOrderIndex(dto.getOrderIndex());
        sectionRepository.save(s);
        return toResponse(s);
    }

    @Override
    public SectionResponseDTO getById(Long id) {
        return toResponse(sectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section", id)));
    }

    @Override
    public List<SectionResponseDTO> getByCourseId(Long courseId) {
        return sectionRepository.findByCourseIdOrderByOrderIndex(courseId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!sectionRepository.existsById(id)) throw new ResourceNotFoundException("Section", id);
        sectionRepository.deleteById(id);
    }

    private SectionResponseDTO toResponse(Section s) {
        SectionResponseDTO r = new SectionResponseDTO();
        r.setId(s.getId());
        r.setTitle(s.getTitle());
        r.setOrderIndex(s.getOrderIndex());
        // leave lectures mapping to controller or map each lecture DTO
        return r;
    }
}
