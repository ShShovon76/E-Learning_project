package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.course.CourseRequestDTO;
import com.example.E_Learning_CRUD.dto.course.CourseResponseDTO;
import com.example.E_Learning_CRUD.entity.Category;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.enums.CourseStatus;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CategoryRepository;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public CourseResponseDTO create(CourseRequestDTO dto) {
        User instructor = userRepository.findById(dto.getInstructorId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getInstructorId()));
        Category cat = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));
        Course c = Course.builder()
                .title(dto.getTitle())
                .subtitle(dto.getSubtitle())
                .description(dto.getDescription())
                .learningObjectives(dto.getLearningObjectives())
                .requirements(dto.getRequirements())
                .targetAudience(dto.getTargetAudience())
                .instructor(instructor)
                .category(cat)
                .level(dto.getLevel())
                .price(dto.getPrice())
                .discountPrice(dto.getDiscountPrice())
                .thumbnailUrl(dto.getThumbnailUrl())
                .promoVideoUrl(dto.getPromoVideoUrl())
                .createdAt(LocalDateTime.now())
                .status(CourseStatus.DRAFT)
                .build();
        courseRepository.save(c);
        return toResponse(c);
    }

    @Override
    public CourseResponseDTO update(Long id, CourseRequestDTO dto) {
        Course c = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course", id));
        c.setTitle(dto.getTitle());
        c.setSubtitle(dto.getSubtitle());
        c.setDescription(dto.getDescription());
        c.setLearningObjectives(dto.getLearningObjectives());
        c.setRequirements(dto.getRequirements());
        c.setTargetAudience(dto.getTargetAudience());
        c.setLevel(dto.getLevel());
        c.setPrice(dto.getPrice());
        c.setDiscountPrice(dto.getDiscountPrice());
        c.setThumbnailUrl(dto.getThumbnailUrl());
        c.setPromoVideoUrl(dto.getPromoVideoUrl());
        c.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(c);
        return toResponse(c);
    }

    @Override
    public CourseResponseDTO getById(Long id) {
        return toResponse(courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course", id)));
    }

    @Override
    public List<CourseResponseDTO> getAll() {
        return courseRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!courseRepository.existsById(id)) throw new ResourceNotFoundException("Course", id);
        courseRepository.deleteById(id);
    }

    private CourseResponseDTO toResponse(Course c) {
        CourseResponseDTO r = new CourseResponseDTO();
        r.setId(c.getId());
        r.setTitle(c.getTitle());
        r.setSubtitle(c.getSubtitle());
        r.setInstructorName(c.getInstructor()!=null ? (c.getInstructor().getFirstName()+" "+c.getInstructor().getLastName()) : null);
        r.setCategoryName(c.getCategory()!=null ? c.getCategory().getName() : null);
        r.setPrice(c.getPrice());
        r.setAverageRating(c.getAverageRating());
        r.setCreatedAt(c.getCreatedAt());
        return r;
    }
}
