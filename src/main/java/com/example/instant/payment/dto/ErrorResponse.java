package com.example.instant.payment.dto;

import lombok.Data;

@Data
public class ErrorResponse {

	private  String errorCode;
	private  String errorMessage;

}
