package com.example.notification.service;

import com.example.notification.event.AccountEvent;
import com.example.notification.event.LoanEvent;
import com.example.notification.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void consumeTransactionEvent(TransactionEvent event) {
        log.info("Received Transaction Event: {}", event);

        String subject = "Transaction Notification";
        String content = String.format("""
            <html>
                <body>
                    <h2>Transaction Alert</h2>
                    <p>Dear Customer,</p>
                    <p>Your transaction with reference %s has been processed.</p>
                    <p><strong>Amount:</strong> %s</p>
                    <p><strong>Type:</strong> %s</p>
                    <p><strong>Status:</strong> %s</p>
                    <p>Thank you for banking with us.</p>
                </body>
            </html>
            """,
            event.getTransactionReference(),
            event.getAmount(),
            event.getType(),
            event.getStatus());

        emailService.sendEmail(event.getEmail(), subject, content);
    }

    @KafkaListener(topics = "account-events", groupId = "notification-group")
    public void consumeAccountEvent(AccountEvent event) {
        log.info("Received Account Event: {}", event);

        String subject = "Account " + event.getEventType() + " Notification";
        String content = String.format("""
            <html>
                <body>
                    <h2>Account %s Notification</h2>
                    <p>Dear Customer,</p>
                    <p>Your account %s has been %s.</p>
                    <p><strong>Account Number:</strong> %s</p>
                    <p><strong>Account Type:</strong> %s</p>
                    <p><strong>Current Balance:</strong> %s</p>
                    <p>Thank you for banking with us.</p>
                </body>
            </html>
            """,
            event.getEventType(),
            event.getAccountNumber(),
            event.getEventType().toLowerCase(),
            event.getAccountNumber(),
            event.getAccountType(),
            event.getBalance());

        emailService.sendEmail(event.getEmail(), subject, content);
    }

    @KafkaListener(topics = "loan-events", groupId = "notification-group")
    public void consumeLoanEvent(LoanEvent event) {
        log.info("Received Loan Event: {}", event);

        String subject = "Loan " + event.getEventType() + " Notification";
        String content = String.format("""
            <html>
                <body>
                    <h2>Loan %s Notification</h2>
                    <p>Dear Customer,</p>
                    <p>Your loan application has been %s.</p>
                    <p><strong>Loan Amount:</strong> %s</p>
                    <p><strong>Tenure:</strong> %s months</p>
                    <p><strong>Current Balance:</strong> %s</p>
                    <p>Thank you for banking with us.</p>
                </body>
            </html>
            """,
            event.getEventType(),
            event.getEventType().toLowerCase(),
            event.getAmount(),
            event.getTenureMonths(),
            event.getBalance());

        emailService.sendEmail(event.getEmail(), subject, content);
    }
}