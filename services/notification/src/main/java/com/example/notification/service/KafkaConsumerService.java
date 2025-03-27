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

    @KafkaListener(
        topics = "transaction-events",
        groupId = "notification-group",
        properties = {
            "spring.json.value.default.type=com.example.notification.event.TransactionEvent"
        }
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

            log.info("event.getEmail(): {}, subject: {}, content: {}", event.getEmail(), subject, content);//            emailService.sendEmail(event.getEmail(), subject, content);

        } catch (Exception e) {
            log.error("Error processing transaction event: {}", event, e);
        }
    }




        @KafkaListener(
            topics = "account-events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
        )
        public void consumeAccountEvent(AccountEvent event) {
            try {
                log.info("Received Account Event: {}", event);

                if (event.getEmail() == null) {
                    log.error("No email in account event: {}", event);
                    return;
                }

                String subject = "Account Notification";
                String content = String.format("""
                Account %s: %s
                Balance: %s %s
                Created: %s""",
                    event.getAccountNumber(),
                    event.getEventType(),
                    event.getBalance(),
                    event.getCurrency(),
                    event.getCreatedAt());

                emailService.sendEmail(event.getEmail(), subject, content);

            } catch (Exception e) {
                log.error("Failed to process account event", e);
                throw e;
            }
        }


//
//    @KafkaListener(topics = "loan-events", groupId = "notification-group")
//    public void consumeLoanEvent(LoanEvent event) {
//        log.info("Received Loan Event: {}", event);
//
//        String subject = "Loan " + event.getEventType() + " Notification";
//        String content = String.format("""
//            <html>
//                <body>
//                    <h2>Loan %s Notification</h2>
//                    <p>Dear Customer,</p>
//                    <p>Your loan application has been %s.</p>
//                    <p><strong>Loan Amount:</strong> %s</p>
//                    <p><strong>Tenure:</strong> %s months</p>
//                    <p><strong>Current Balance:</strong> %s</p>
//                    <p>Thank you for banking with us.</p>
//                </body>
//            </html>
//            """,
//            event.getEventType(),
//            event.getEventType().toLowerCase(),
//            event.getAmount(),
//            event.getTenureMonths(),
//            event.getBalance());
//
//        emailService.sendEmail(event.getEmail(), subject, content);
//    }
    }