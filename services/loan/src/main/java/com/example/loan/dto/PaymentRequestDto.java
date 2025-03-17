package com.example.loan.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    private Long loanId;
    private BigDecimal paymentAmount;
}