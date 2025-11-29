package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
