package com.example.transaction.account;

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
