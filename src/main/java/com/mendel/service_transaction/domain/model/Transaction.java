package com.mendel.service_transaction.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {
	private final Long id; 
	private final BigDecimal amount;
	private final String type;
	private final Long parentId;
}