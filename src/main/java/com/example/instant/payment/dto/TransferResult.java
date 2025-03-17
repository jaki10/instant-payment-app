package com.example.instant.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResult {
	
	private  Long  accountFromId;
	private  BigDecimal  balanceAfterTransfer;

}
