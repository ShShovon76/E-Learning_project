package com.example.E_Learning_CRUD.entity;

import com.example.E_Learning_CRUD.enums.LectureType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "lectures")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lecture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @JsonIgnore
    private Section section;

    private String title;

    @Column(length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    private LectureType type;

    private String contentUrl;     // video URL / article storage reference
    private String thumbnailUrl;
    private Long durationSeconds;
    private Boolean preview = false;
    private Integer orderIndex;

    @Column(length = 2000)
    private String attachmentsJson;
}
