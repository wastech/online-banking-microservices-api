package com.example.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEvent {
    private Long authId;
    private Long accountId;
    private BigDecimal amount;
    private Integer tenureMonths;
    private BigDecimal balance;
    private String status;
    private String email;
    private String eventType; // APPROVED, REPAYMENT, OVERDUE, etc.
}