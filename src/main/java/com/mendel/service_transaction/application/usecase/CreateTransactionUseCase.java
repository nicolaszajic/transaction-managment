package com.mendel.service_transaction.application.usecase;

import java.math.BigDecimal;

public interface CreateTransactionUseCase {
	void createTransaction(Long id, BigDecimal amount, String type, Long parentId);
}