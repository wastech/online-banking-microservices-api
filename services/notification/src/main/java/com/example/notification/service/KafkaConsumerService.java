// The bug is in the content format for the account event email.
// In KafkaConsumerService.java, the email content for AccountEvent is plain text
// but should be HTML format like the TransactionEvent

package com.example.notification.service;

import com.example.notification.event.AccountEvent;
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

    @KafkaListener(
        topics = "transaction-events",
        groupId = "notification-group",
        containerFactory = "transactionKafkaListenerContainerFactory"
    )
    public void consumeTransactionEvent(TransactionEvent event) {
        try {
            log.info("Received Transaction Event: {}", event);

            if (event.getEmail() == null || event.getEmail().isEmpty()) {
                log.warn("No email address provided in event: {}", event);
                return;
            }

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

            log.info("event.getEmail(): {}, subject: {}, content: {}", event.getEmail(), subject, content);
//            emailService.sendEmail(event.getEmail(), subject, content);

        } catch (Exception e) {
            log.error("Error processing transaction event: {}", event, e);
        }
    }

    @KafkaListener(
        topics = "account-events",
        groupId = "notification-group",
        containerFactory = "accountKafkaListenerContainerFactory"
    )
    public void consumeAccountEvent(AccountEvent event) {
        try {
            log.info("Received Account Event: {}", event);

            if (event.getEmail() == null) {
                log.error("No email in account event: {}", event);
                return;
            }

            String subject = "Account Notification";
            // FIXED: Changed from plain text to HTML format to match expected email format
            String content = String.format("""
                <html>
                    <body>
                        <h2>Account Notification</h2>
                        <p>Dear Customer,</p>
                        <p>We're writing to inform you about your account:</p>
                        <p><strong>Account Number:</strong> %s</p>
                        <p><strong>Event:</strong> %s</p>
                        <p><strong>Balance:</strong> %s %s</p>
                        <p><strong>Created:</strong> %s</p>
                        <p>Thank you for banking with us.</p>
                    </body>
                </html>
                """,
                event.getAccountNumber(),
                event.getEventType(),
                event.getBalance(),
                event.getCurrency(),
                event.getCreatedAt());

//            emailService.sendEmail(event.getEmail(), subject, content);
            log.info("event.getEmail(): {}, subject: {}, content: {}", event.getEmail(), subject, content);

        } catch (Exception e) {
            log.error("Failed to process account event", e);
            throw e; // Re-throwing the exception
        }
    }
}