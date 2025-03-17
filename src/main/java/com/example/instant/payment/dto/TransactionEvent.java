package com.example.instant.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {

    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;

}
