package com.example.E_Learning_CRUD.entity;

import com.example.E_Learning_CRUD.enums.QuizType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quiz {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;
    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private QuizType type;

    private Integer timeLimitMinutes;
    private Integer maxAttempts;
    private Double passingScore;
    private Boolean shuffleQuestions = false;
    private Boolean showResults = true;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();


}
