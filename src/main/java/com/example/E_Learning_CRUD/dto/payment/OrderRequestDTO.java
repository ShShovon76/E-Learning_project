package com.example.E_Learning_CRUD.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long studentId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
}
