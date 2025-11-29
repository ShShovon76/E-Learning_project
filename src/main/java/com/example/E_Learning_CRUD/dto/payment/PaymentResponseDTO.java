package com.example.E_Learning_CRUD.dto.payment;

import com.example.E_Learning_CRUD.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}
