package com.example.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent {
    private String accountNumber;
    private Long userId;
    private String accountType;
    private BigDecimal balance;
    private String email;
    private String eventType; // CREATED, UPDATED, etc.
}