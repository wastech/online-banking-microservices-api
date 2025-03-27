package com.example.transaction.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TransactionEvent {
    private String transactionReference;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private String type;  // Enum values: DEPOSIT, WITHDRAWAL, TRANSFER
    private String status; // Enum values: PENDING, COMPLETED, FAILED, REVERSED
    private String description;
    private Long userId;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Optional: Add builder pattern for easier construction
    public static TransactionEventBuilder builder() {
        return new TransactionEventBuilder();
    }

    public static class TransactionEventBuilder {
        private String transactionReference;
        private Long sourceAccountId;
        private Long destinationAccountId;
        private BigDecimal amount;
        private String type;
        private String status;
        private String description;
        private Long userId;
        private String email;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public TransactionEventBuilder transactionReference(String transactionReference) {
            this.transactionReference = transactionReference;
            return this;
        }

        public TransactionEventBuilder sourceAccountId(Long sourceAccountId) {
            this.sourceAccountId = sourceAccountId;
            return this;
        }

        // ... similar builder methods for all fields ...

        public TransactionEvent build() {
            TransactionEvent event = new TransactionEvent();
            event.setTransactionReference(transactionReference);
            event.setSourceAccountId(sourceAccountId);
            event.setDestinationAccountId(destinationAccountId);
            event.setAmount(amount);
            event.setType(type);
            event.setStatus(status);
            event.setDescription(description);
            event.setUserId(userId);
            event.setEmail(email);
            event.setCreatedAt(createdAt);
            event.setUpdatedAt(updatedAt);
            return event;
        }
    }
}