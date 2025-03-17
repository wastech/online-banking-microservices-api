package com.example.loan.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequestDto {
    private Long authId;
    private Long accountId;
    private BigDecimal amount;
    private Integer tenureMonths;
}