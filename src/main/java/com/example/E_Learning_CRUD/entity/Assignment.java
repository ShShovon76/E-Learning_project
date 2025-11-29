package com.example.E_Learning_CRUD.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String title;

    @Column(length = 4000)
    private String description;

    @Column(length = 2000)
    private String instructions;

    private LocalDateTime dueDate;
    private Integer maxPoints =100;
    private String submissionFormat; // FILE, TEXT, BOTH

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AssignmentSubmission> submissions = new ArrayList<>();
}
