package com.example.instant.payment.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "TRANSACTIONSS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTIONID")
	@ApiModelProperty(hidden = true)
	private Long transactionId;
	@NotNull
	@Column(name = "ACCOUNTFROMID")
	private Long senderId;

	@NotNull
	@Column(name = "ACCOUNTTOID")
	private Long receiverId;

	@NotNull
	@Column(name = "AMOUNT")
	@Min(value = 0, message = "Transfer amount can not be less than zero")
	private BigDecimal amount;

}
