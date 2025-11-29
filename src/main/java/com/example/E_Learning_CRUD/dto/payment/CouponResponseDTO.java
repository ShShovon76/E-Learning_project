package com.example.E_Learning_CRUD.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponseDTO {
    private Long id;
    private String code;
    private BigDecimal discountValue;
    private Boolean active;
}
