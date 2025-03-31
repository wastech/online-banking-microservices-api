package com.example.transaction.service;

import com.example.transaction.account.AccountClient;
import com.example.transaction.auth.AuthClient;
import com.example.transaction.event.TransactionEvent;
import com.example.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AuthClient authClient;
    private final AccountClient accountClient;

    // Remove the KafkaListener annotation from here - don't both produce and consume on the same topic
    // in the same service unless you need to

    public void publishTransactionEvent(Transaction transaction) {
        try {
            var account = accountClient.findAccountById(transaction.getSourceAccountId());
            Long userId = account.get().userId(); // Ensure AccountClient returns a valid userId

            authClient.findCustomerById(userId)
                .ifPresentOrElse(
                    user -> {
                        TransactionEvent event = TransactionEvent.builder()
                            .transactionReference(transaction.getTransactionReference())
                            .sourceAccountId(transaction.getSourceAccountId())
                            .destinationAccountId(transaction.getDestinationAccountId())
                            .amount(transaction.getAmount())
                            .type(transaction.getType().name())
                            .status(transaction.getStatus().name())
                            .description(transaction.getDescription())
                            .userId(userId)
                            .email(user.email())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                        log.info("Publishing TransactionEvent with email: {}", event.getEmail());
                        kafkaTemplate.send("transaction-events", event);
                    },
                    () -> log.warn("User not found for user ID: {}", userId)
                );

        } catch (Exception e) {
            log.error("Error processing transaction event: {}", e.getMessage(), e);
        }
    }
}