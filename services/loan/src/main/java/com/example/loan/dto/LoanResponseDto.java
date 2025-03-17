package com.example.loan.dto;

import com.example.loan.model.Loan;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanResponseDto {
    private Long loanId;
    private Long authId;
    private Long accountId;
    private BigDecimal amount;
    private Integer tenureMonths;
    private BigDecimal balance;
    private LocalDate dueDate;
    private BigDecimal interest;
    private Loan.LoanStatus status;
}