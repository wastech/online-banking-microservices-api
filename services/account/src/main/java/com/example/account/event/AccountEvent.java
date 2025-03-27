package com.example.account.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AccountEvent {
    private String eventType;
    private String accountNumber;
    private Long userId;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
    private String email;
}