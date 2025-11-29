package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.interaction.CommentRequestDTO;
import com.example.E_Learning_CRUD.dto.interaction.CommentResponseDTO;
import com.example.E_Learning_CRUD.entity.Comment;
import com.example.E_Learning_CRUD.entity.Discussion;
import com.example.E_Learning_CRUD.entity.User;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CommentRepository;
import com.example.E_Learning_CRUD.repository.DiscussionRepository;
import com.example.E_Learning_CRUD.repository.UserRepository;
import com.example.E_Learning_CRUD.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDTO create(CommentRequestDTO dto) {
        Discussion discussion = discussionRepository.findById(dto.getDiscussionId()).orElseThrow(() -> new ResourceNotFoundException("Discussion", dto.getDiscussionId()));
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", dto.getUserId()));
        Comment c = new Comment();
        c.setDiscussion(discussion);
        c.setUser(user);
        c.setContent(dto.getContent());
        if(dto.getParentCommentId()!=null) {
            Comment parent = commentRepository.findById(dto.getParentCommentId()).orElseThrow(() -> new ResourceNotFoundException("Comment", dto.getParentCommentId()));
            c.setParentComment(parent);
        }
        c.setCreatedAt(LocalDateTime.now());
        commentRepository.save(c);
        CommentResponseDTO r = new CommentResponseDTO();
        r.setId(c.getId());
        r.setContent(c.getContent());
        r.setCreatedAt(c.getCreatedAt());
        return r;
    }

    @Override
    public List<CommentResponseDTO> getByDiscussionId(Long discussionId) {
        return commentRepository.findByDiscussionIdOrderByCreatedAtAsc(discussionId).stream().map(c -> {
            CommentResponseDTO r = new CommentResponseDTO();
            r.setId(c.getId());
            r.setContent(c.getContent());
            r.setCreatedAt(c.getCreatedAt());
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!commentRepository.existsById(id)) throw new ResourceNotFoundException("Comment", id);
        commentRepository.deleteById(id);
    }
}
