package com.example.instant.payment.controller;

import com.example.instant.payment.dto.TransferResult;
import com.example.instant.payment.exception.AccountNotExistException;
import com.example.instant.payment.exception.OverDraftException;
import com.example.instant.payment.model.Transaction;
import com.example.instant.payment.service.AccountsService;
import com.example.instant.payment.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
@Api(tags = {"Transaction Controller"}, description = "Provide APIs for transaction related operation")
@Slf4j
public class TransactionController {

	@Autowired
	private AccountsService accountService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping
	@ApiOperation(value = "Get all transactions", response = List.class, produces = "application/json")
	public ResponseEntity<List<Transaction>> getAllTransactions() {
		List<Transaction> allTransactions = transactionService.findAll();

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(allTransactions);
	}

	@PostMapping(consumes = { "application/json" })
	@ApiOperation(value = "API to create a transaction", response = TransferResult.class, produces = "application/json")
	@Transactional
	public ResponseEntity transferMoney(@RequestBody @Valid Transaction transactionRequest) throws Exception {

		try {
			accountService.transferBalances(transactionRequest);
			transactionService.createTransaction(transactionRequest);

			Long accountId = transactionRequest.getSenderId();

			TransferResult result = new TransferResult();
			result.setAccountFromId(accountId);
			result.setBalanceAfterTransfer(accountService.retrieveAccountById(accountId).getBalance());
			
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (AccountNotExistException | OverDraftException e) {
			log.error("Fail to transfer balances, please check with system administrator.");
			throw e;
		}
	}
}
