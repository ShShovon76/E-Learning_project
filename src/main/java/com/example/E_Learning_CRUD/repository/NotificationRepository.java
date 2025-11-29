package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
