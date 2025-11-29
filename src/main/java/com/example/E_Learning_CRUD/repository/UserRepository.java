package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
