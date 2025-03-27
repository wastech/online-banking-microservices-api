package com.example.account.service;

import com.example.account.event.AccountEvent;
import com.example.account.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEventPublisher {

    private final KafkaTemplate<String, AccountEvent> kafkaTemplate;

    public void publishAccountCreatedEvent(
        String accountNumber,
        Long userId,
        Account.AccountType accountType,
        BigDecimal balance,
        String currency,
        LocalDateTime createdAt,
        String email
    ) {
        AccountEvent event = AccountEvent.builder()
            .eventType("ACCOUNT_CREATED")
            .accountNumber(accountNumber)
            .userId(userId)
            .accountType(accountType.name())
            .balance(balance)
            .currency(currency)
            .createdAt(createdAt)
            .email(email)
            .build();

        kafkaTemplate.send("account-events", event);
        log.info("Published AccountEvent: {}", event);
    }
}