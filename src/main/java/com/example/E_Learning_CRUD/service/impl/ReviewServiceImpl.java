package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.interaction.ReviewRequestDTO;
import com.example.E_Learning_CRUD.dto.interaction.ReviewResponseDTO;
import com.example.E_Learning_CRUD.entity.Course;
import com.example.E_Learning_CRUD.entity.Review;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CourseRepository;
import com.example.E_Learning_CRUD.repository.ReviewRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public ReviewResponseDTO create(ReviewRequestDTO dto) {
        User student = userRepository.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getStudentId()));
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course", dto.getCourseId()));
        Review r = new Review();
        r.setStudent(student);
        r.setCourse(course);
        r.setRating(dto.getRating());
        r.setComment(dto.getComment());
        r.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(r);
        ReviewResponseDTO res = new ReviewResponseDTO();
        res.setId(r.getId());
        res.setRating(r.getRating());
        res.setComment(r.getComment());
        res.setCreatedAt(r.getCreatedAt());
        return res;
    }

    @Override
    public List<ReviewResponseDTO> getByCourseId(Long courseId) {
        return reviewRepository.findByCourseId(courseId).stream().map(r -> {
            ReviewResponseDTO res = new ReviewResponseDTO();
            res.setId(r.getId());
            res.setRating(r.getRating());
            res.setComment(r.getComment());
            res.setCreatedAt(r.getCreatedAt());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!reviewRepository.existsById(id)) throw new ResourceNotFoundException("Review", id);
        reviewRepository.deleteById(id);
    }
}
