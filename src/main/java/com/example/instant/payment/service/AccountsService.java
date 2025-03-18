package com.example.instant.payment.service;

import com.example.instant.payment.constant.ErrorCode;
import com.example.instant.payment.dto.TransactionEvent;
import com.example.instant.payment.exception.AccountNotExistException;
import com.example.instant.payment.exception.OverDraftException;
import com.example.instant.payment.model.Account;
import com.example.instant.payment.model.Transaction;
import com.example.instant.payment.repository.AccountsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;

@Service
@Slf4j
public class AccountsService {
	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	public List<Account> findAll() {
		return accountsRepository.findAll();
	}
	
	public Account retrieveAccountById(Long accountId) {
		Account account = accountsRepository.findByAccountId(accountId)
		.orElseThrow(() -> new AccountNotExistException("Account with id:" + accountId + " does not exist.",
				ErrorCode.ACCOUNT_ERROR, HttpStatus.NOT_FOUND));
		
		return account;
	}

	public Account createAccount(Account account) {
		return accountsRepository.save(account);
	}
	
	@Transactional
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public void transferBalances(Transaction transfer) throws OverDraftException, AccountNotExistException {
		Account senderAccount = getAccountFromTransferInfo(transfer.getSenderId());
		Account receiverAccount = getAccountFromTransferInfo(transfer.getReceiverId());

		if(senderAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
			throw new OverDraftException("Account with id:" + senderAccount.getAccountId() +
					" does not have enough balance to transfer.", ErrorCode.ACCOUNT_ERROR);
		}
		
		senderAccount.setBalance(senderAccount.getBalance().subtract(transfer.getAmount()));
		receiverAccount.setBalance(receiverAccount.getBalance().add(transfer.getAmount()));

		accountsRepository.save(senderAccount);
		accountsRepository.save(receiverAccount);

		// Publish Kafka event
		TransactionEvent event = new TransactionEvent(senderAccount.getAccountId(),
				receiverAccount.getAccountId(), transfer.getAmount());
		kafkaProducerService.sendTransferEvent(event);
	}

	private Account getAccountFromTransferInfo(Long idFromTransferInfo) {
		return accountsRepository.getAccountForUpdate(idFromTransferInfo)
				.orElseThrow(() -> new AccountNotExistException("Account with id:" + idFromTransferInfo +
						" does not exist.", ErrorCode.ACCOUNT_ERROR));
	}
}
