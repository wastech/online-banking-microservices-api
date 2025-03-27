package com.example.transaction.service;

import com.example.transaction.auth.AuthClient;
import com.example.transaction.event.TransactionEvent;
import com.example.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionEventPublisher {
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final AuthClient authClient;

    @KafkaListener(
        topics = "transaction-events",
        groupId = "notification-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void publishTransactionEvent(Transaction transaction) {
        authClient.findCustomerById(transaction.getSourceAccountId())
            .ifPresentOrElse(
                user -> {
                    log.info("Publishing TransactionEvent with email: {}", user); // Debug log

                    // Use the Builder pattern (or set fields directly)
                    TransactionEvent event = TransactionEvent.builder()
                        .transactionReference(transaction.getTransactionReference())
                        .sourceAccountId(transaction.getSourceAccountId())
                        .destinationAccountId(transaction.getDestinationAccountId())
                        .amount(transaction.getAmount())
                        .type(transaction.getType().name())
                        .status(transaction.getStatus().name())
                        .description(transaction.getDescription())
                        .userId(Long.parseLong(user.id()))
                        .email(user.email()) // <-- Make sure this is set!
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                    log.info("Publishing TransactionEvent with email: {}", event.getEmail()); // Debug log
                    kafkaTemplate.send("transaction-events", event);
                },
                () -> log.warn("User not found for account ID: {}", transaction.getSourceAccountId())
            );
    }
}