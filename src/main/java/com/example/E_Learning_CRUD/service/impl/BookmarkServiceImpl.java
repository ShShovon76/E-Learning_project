package com.example.E_Learning_CRUD.service.impl;


import com.example.E_Learning_CRUD.dto.learning.BookmarkRequestDTO;
import com.example.E_Learning_CRUD.dto.learning.BookmarkResponseDTO;
import com.example.E_Learning_CRUD.entity.Bookmark;
import com.example.E_Learning_CRUD.entity.Lecture;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.BookmarkRepository;
import com.example.E_Learning_CRUD.repository.LectureRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    @Override
    public BookmarkResponseDTO create(BookmarkRequestDTO dto) {
        User student = userRepository.findById(dto.getStudentId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getStudentId()));
        Lecture lecture = lectureRepository.findById(dto.getLectureId()).orElseThrow(() -> new ResourceNotFoundException("Lecture", dto.getLectureId()));
        Bookmark b = new Bookmark();
        b.setStudent(student);
        b.setLecture(lecture);
        b.setNote(dto.getNote());
        b.setCreatedAt(LocalDateTime.now());
        bookmarkRepository.save(b);
        return toResponse(b);
    }

    @Override
    public List<BookmarkResponseDTO> getByStudentId(Long studentId) {
        return bookmarkRepository.findByStudentId(studentId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!bookmarkRepository.existsById(id)) throw new ResourceNotFoundException("Bookmark", id);
        bookmarkRepository.deleteById(id);
    }

    private BookmarkResponseDTO toResponse(Bookmark b) {
        BookmarkResponseDTO r = new BookmarkResponseDTO();
        r.setId(b.getId());
        r.setLectureId(b.getLecture().getId());
        r.setNote(b.getNote());
        return r;
    }
}
