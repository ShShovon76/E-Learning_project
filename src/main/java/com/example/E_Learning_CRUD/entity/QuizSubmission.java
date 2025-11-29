package com.example.E_Learning_CRUD.entity;

import com.example.E_Learning_CRUD.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_submissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "quiz_id", "attempt_number"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizSubmission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    private Integer attemptNumber;
    private Double score;
    private Integer timeSpentSeconds;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionResponse> responses = new ArrayList<>();
}
