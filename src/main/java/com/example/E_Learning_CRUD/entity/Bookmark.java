package com.example.E_Learning_CRUD.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "lecture_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Bookmark {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(length = 1000)
    private String note;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
