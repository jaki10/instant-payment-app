package com.example.instant.payment.service;

import com.example.instant.payment.dto.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
    public static final String MONEY_TRANSFER_TOPIC = "money-transfer-topic";
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransferEvent(TransactionEvent event) {
        kafkaTemplate.send(MONEY_TRANSFER_TOPIC, event);
        log.info("Producer produced the message {}", event);
    }
}
