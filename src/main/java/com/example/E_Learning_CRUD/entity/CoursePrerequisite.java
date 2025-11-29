package com.example.E_Learning_CRUD.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "course_prerequisites")
@Data
public class CoursePrerequisite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "prerequisite_course_id", nullable = false)
    private Course prerequisiteCourse;

}
