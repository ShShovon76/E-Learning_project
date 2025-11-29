package com.example.E_Learning_CRUD.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Progress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonIgnore
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    // 0.0 - 1.0 or 0 -100 depending on convention
    private Double progressPercentage;

    private Long watchedSeconds;
    private Long lectureTotalSeconds;
    private Boolean completed = false;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime lastWatchedAt;
}
