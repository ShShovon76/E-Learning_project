package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
