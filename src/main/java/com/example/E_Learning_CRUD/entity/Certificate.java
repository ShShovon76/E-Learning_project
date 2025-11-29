package com.example.E_Learning_CRUD.entity;


import com.example.E_Learning_CRUD.enums.CertificateStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "certificates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"certificate_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Certificate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_id", unique = true, nullable = false)
    private String certificateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String instructorName;

    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;

    private Double finalGrade;

    private String gradeLetter;

    private String certificateUrl;

    private String qrCodeUrl;

    @Column(unique = true)
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    private CertificateStatus status = CertificateStatus.ACTIVE;
}
