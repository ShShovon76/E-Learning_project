package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.quiz.AssignmentRequestDTO;
import com.example.E_Learning_CRUD.dto.quiz.AssignmentResponseDTO;
import com.example.E_Learning_CRUD.entity.Assignment;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.AssignmentRepository;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    @Override
    public AssignmentResponseDTO create(AssignmentRequestDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course", dto.getCourseId()));
        Assignment a = new Assignment();
        a.setCourse(course);
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setDueDate(dto.getDueDate());
        assignmentRepository.save(a);
        AssignmentResponseDTO r = new AssignmentResponseDTO();
        r.setId(a.getId());
        r.setTitle(a.getTitle());
        r.setDueDate(a.getDueDate());
        return r;
    }

    @Override
    public AssignmentResponseDTO update(Long id, AssignmentRequestDTO dto) {
        Assignment a = assignmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Assignment", id));
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setDueDate(dto.getDueDate());
        assignmentRepository.save(a);
        AssignmentResponseDTO r = new AssignmentResponseDTO();
        r.setId(a.getId());
        r.setTitle(a.getTitle());
        r.setDueDate(a.getDueDate());
        return r;
    }

    @Override
    public AssignmentResponseDTO getById(Long id) {
        Assignment a = assignmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Assignment", id));
        AssignmentResponseDTO r = new AssignmentResponseDTO();
        r.setId(a.getId());
        r.setTitle(a.getTitle());
        r.setDueDate(a.getDueDate());
        return r;
    }

    @Override
    public List<AssignmentResponseDTO> getByCourseId(Long courseId) {
        return assignmentRepository.findByCourseId(courseId).stream().map(a -> {
            AssignmentResponseDTO r = new AssignmentResponseDTO();
            r.setId(a.getId());
            r.setTitle(a.getTitle());
            r.setDueDate(a.getDueDate());
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!assignmentRepository.existsById(id)) throw new ResourceNotFoundException("Assignment", id);
        assignmentRepository.deleteById(id);
    }
}
