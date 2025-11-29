package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDiscussionIdOrderByCreatedAtAsc(Long discussionId);
}
