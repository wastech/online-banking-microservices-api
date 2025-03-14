package com.example.transaction.dto;

import com.example.transaction.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {
    private Long id;
    private String transactionReference;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private Transaction.TransactionType type;
    private Transaction.TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
}