package com.example.E_Learning_CRUD.entity;


import com.example.E_Learning_CRUD.enums.CourseLevel;
import com.example.E_Learning_CRUD.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(length = 4000)
    private String description;

    @Column(length = 2000)
    private String learningObjectives;

    @Column(length = 2000)
    private String requirements;

    private String targetAudience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    @JsonIgnore
    private User instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.DRAFT;

    private String language;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal discountPrice;

    private String thumbnailUrl;
    private String promoVideoUrl;

    // store total duration in seconds or ISO-8601 format via Duration
    private Long totalDurationSeconds;

    private Integer totalLectures;
    private Integer totalStudents;
    private Double averageRating;
    private Integer totalReviews;
    private boolean featured = false;
    private boolean approved = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    // Sections
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    // prerequisites (many-to-many self reference)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_course_id"))
    private Set<Course> prerequisites = new HashSet<>();
}
