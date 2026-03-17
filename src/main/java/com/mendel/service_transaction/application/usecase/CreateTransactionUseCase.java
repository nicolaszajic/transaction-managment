package com.mendel.service_transaction.application.usecase;

import com.mendel.service_transaction.application.model.CreateTransactionCommand;

public interface CreateTransactionUseCase {
	void createTransaction(CreateTransactionCommand command);
}