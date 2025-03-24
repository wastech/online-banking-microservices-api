package com.example.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic transactionEventsTopic() {
        return TopicBuilder.name("transaction-events")
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic accountEventsTopic() {
        return TopicBuilder.name("account-events")
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic loanEventsTopic() {
        return TopicBuilder.name("loan-events")
            .partitions(3)
            .replicas(1)
            .build();
    }
}