package com.example.instant.payment.exception;

import org.springframework.http.HttpStatus;

public class AccountNotExistException extends BusinessException {

	public AccountNotExistException(String message, String errorCode) {
		super(message, errorCode);
	}
	
	public AccountNotExistException(String message, String errorCode, HttpStatus httpStatus) {
		super(message, errorCode, httpStatus);
	}
}
