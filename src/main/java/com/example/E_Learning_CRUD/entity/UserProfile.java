package com.example.E_Learning_CRUD.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class UserProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    private String address;
    private String city;
    private String country;
    private String timezone;
    private String language;
    private boolean emailNotifications = true;
    private boolean pushNotifications = true;

    // instructor-specific fields (nullable for students)
    private String title;         // e.g., "Dr.", "Prof."
    private String expertise;     // comma separated or another normalized table
    private String website;
    private String linkedin;
    private boolean verified = false;
    private BigDecimal totalEarnings;
    private LocalDate dateOfBirth;
}
