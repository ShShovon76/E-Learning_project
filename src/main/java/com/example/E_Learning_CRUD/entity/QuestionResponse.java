package com.example.E_Learning_CRUD.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_responses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionResponse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private QuizSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String answer; // could store answer id(s) or text

    private Boolean isCorrect;
    private Double pointsEarned;
    private String feedback;
}
