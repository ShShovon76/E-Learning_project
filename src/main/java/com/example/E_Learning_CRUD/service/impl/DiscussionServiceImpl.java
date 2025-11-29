package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.interaction.DiscussionRequestDTO;
import com.example.E_Learning_CRUD.dto.interaction.DiscussionResponseDTO;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.entity.Discussion;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.repository.DiscussionRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.DiscussionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public DiscussionResponseDTO create(DiscussionRequestDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course", dto.getCourseId()));
        User student = userRepository.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getStudentId()));
        Discussion d = new Discussion();
        d.setCourse(course);
        d.setStudent(student);
        d.setTitle(dto.getTitle());
        d.setContent(dto.getContent());
        d.setType(dto.getType());
        d.setCreatedAt(LocalDateTime.now());
        discussionRepository.save(d);
        DiscussionResponseDTO r = new DiscussionResponseDTO();
        r.setId(d.getId());
        r.setTitle(d.getTitle());
        r.setContent(d.getContent());
        return r;
    }

    @Override
    public List<DiscussionResponseDTO> getByCourseId(Long courseId) {
        return discussionRepository.findByCourseId(courseId).stream().map(d -> {
            DiscussionResponseDTO r = new DiscussionResponseDTO();
            r.setId(d.getId());
            r.setTitle(d.getTitle());
            r.setContent(d.getContent());
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!discussionRepository.existsById(id)) throw new ResourceNotFoundException("Discussion", id);
        discussionRepository.deleteById(id);
    }
}
