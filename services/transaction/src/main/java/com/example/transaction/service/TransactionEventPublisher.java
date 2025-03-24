//package com.example.transaction.service;
//
//import com.example.transaction.model.Transaction;
//import com.example.transaction.event.TransactionEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TransactionEventPublisher {
//
//    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
//    private final UserServiceClient userServiceClient; // You'll need to implement this Feign client
//
//    public void publishTransactionEvent(Transaction transaction) {
//        // Fetch user details - you'll need to implement this
//        UserDetails user = userServiceClient.getUserDetails(transaction.getSourceAccountId());
//
//        TransactionEvent event = new TransactionEvent();
//        event.setTransactionReference(transaction.getTransactionReference());
//        event.setSourceAccountId(transaction.getSourceAccountId());
//        event.setDestinationAccountId(transaction.getDestinationAccountId());
//        event.setAmount(transaction.getAmount());
//        event.setType(transaction.getType().name());
//        event.setStatus(transaction.getStatus().name());
//        event.setDescription(transaction.getDescription());
//        event.setUserId(user.getUserId());
//        event.setEmail(user.getEmail());
//
//        kafkaTemplate.send("transaction-events", event);
//    }
//}