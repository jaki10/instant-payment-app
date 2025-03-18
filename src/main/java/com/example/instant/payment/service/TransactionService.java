package com.example.instant.payment.service;

import com.example.instant.payment.model.Transaction;
import com.example.instant.payment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
