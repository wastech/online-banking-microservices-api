package com.example.transaction.dto;

import com.example.transaction.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDto {

    @NotNull
    private Long sourceAccountId;

    private Long destinationAccountId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Transaction.TransactionType type;

    private String description;
}
