package com.example.instant.payment.service;

import com.example.instant.payment.dto.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;

@Service
@Slf4j
public class KafkaConsumerService {
    @KafkaListener(topics = "money-transfer-topic", groupId = "notification-group")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void listenForTransfers(ConsumerRecord<String, TransactionEvent> record) {
        TransactionEvent event = record.value();
        log.info("Sending notification to account " + event.getReceiverId() +
                " for transfer of " + event.getAmount() + " from account " + event.getSenderId());
        //TODO: Here we can implement some email/sms notification logic towards the receiver
    }
}
