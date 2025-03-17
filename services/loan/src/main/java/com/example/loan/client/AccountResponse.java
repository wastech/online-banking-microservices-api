package com.example.loan.client;

import java.math.BigDecimal;

public record AccountResponse (
    Long id,
    String accountNumber,
    Long userId,
    BigDecimal balance,
    String currency,
    boolean isActive

){
}
