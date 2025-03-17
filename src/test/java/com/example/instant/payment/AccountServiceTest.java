package com.example.instant.payment;

import com.example.instant.payment.exception.AccountNotExistException;
import com.example.instant.payment.exception.OverDraftException;
import com.example.instant.payment.model.Account;
import com.example.instant.payment.model.Transaction;
import com.example.instant.payment.repository.AccountsRepository;
import com.example.instant.payment.service.AccountsService;
import com.example.instant.payment.service.KafkaProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock
	AccountsRepository accountsRepository;

	@Mock
	KafkaProducerService kafkaProducerService;

	@InjectMocks
	AccountsService accountsService;

	@Test
	public void testretrieveAccountById() {
		when(accountsRepository.findByAccountId(1L)).thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));

		assertEquals(BigDecimal.ONE, accountsService.retrieveAccountById(1L).getBalance());
	}

	@Test(expected = AccountNotExistException.class)
	public void testRetrieveBalanceFromInvalidAccount() {
		when(accountsRepository.findByAccountId(1L)).thenReturn(Optional.empty());

		accountsService.retrieveAccountById(1L);
	}

	@Test
	public void testTransferBalance() {
		Long accountFromId = 1L;
		Long accountToId = 2L;
		BigDecimal amount = new BigDecimal(10);

		Transaction transactionRequest = new Transaction();
		transactionRequest.setSenderId(accountFromId);
		transactionRequest.setReceiverId(accountToId);
		transactionRequest.setAmount(amount);

		Account accountFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accountTo = new Account(accountFromId, BigDecimal.TEN);

		when(accountsRepository.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accountFrom));
		when(accountsRepository.getAccountForUpdate(accountToId)).thenReturn(Optional.of(accountTo));

		accountsService.transferBalances(transactionRequest);

		assertEquals(BigDecimal.ZERO, accountFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accountTo.getBalance());
	}

	@Test(expected = OverDraftException.class)
	public void testOverdraftBalance() throws OverDraftException, AccountNotExistException {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(20);

		Transaction request = new Transaction();
		request.setSenderId(accountFromId);
		request.setReceiverId(accountFromTo);
		request.setAmount(amount);

		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);

		when(accountsRepository.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accountsRepository.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));

		accountsService.transferBalances(request);
	}
}
