package com.example.E_Learning_CRUD.repository;

import com.example.E_Learning_CRUD.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
