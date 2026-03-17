package com.mendel.service_transaction.application.service;

import java.math.BigDecimal;

import com.mendel.service_transaction.application.model.CreateTransactionCommand;
import com.mendel.service_transaction.application.repository.TransactionRepository;
import com.mendel.service_transaction.application.usecase.CreateTransactionUseCase;
import com.mendel.service_transaction.domain.model.Transaction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

	private final TransactionRepository transactionRepository;

	@Override
	public void createTransaction(CreateTransactionCommand command) {
		Transaction transaction = Transaction.builder().id(command.id()).amount(new BigDecimal(command.amount())).type(command.type())
				.parentId(command.parentId()).build();

		transactionRepository.save(transaction);
	}
}