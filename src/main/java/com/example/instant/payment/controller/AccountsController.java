package com.example.instant.payment.controller;

import com.example.instant.payment.model.Account;
import com.example.instant.payment.service.AccountsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
@Api(tags = { "Accounts Controller" }, description = "Provide APIs for account related operation")
public class AccountsController {

	@Autowired
	private AccountsService accountService;

	@GetMapping
	@ApiOperation(value = "Get all accounts", response = List.class, produces = "application/json")
	public ResponseEntity<List<Account>> getAllAccounts() {
		List<Account> allAccounts = accountService.findAll();

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(allAccounts);
	}

	@GetMapping("/{accountId}")
	@ApiOperation(value = "Get account by id", response = Account.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
							@ApiResponse(code = 404, message = "Account not found with ID")})
	public ResponseEntity<Account> getAccountById(
			@ApiParam(value = "ID related to the account", required = true) @PathVariable Long accountId) {
		Account account = accountService.retrieveAccountById(accountId);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(account);
	}

	@PostMapping(consumes = { "application/json" })
	@ApiOperation(value = "Create new user account", response = Account.class, produces = "application/json")
	public ResponseEntity<Account> createAccount(@RequestBody @Valid Account account) {
		Account savedAccount = accountService.createAccount(account);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
	}
}
