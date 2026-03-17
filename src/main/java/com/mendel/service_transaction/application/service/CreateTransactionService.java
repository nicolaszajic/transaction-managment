package com.mendel.service_transaction.application.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.mendel.service_transaction.application.model.CreateTransactionCommand;
import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.application.usecase.CreateTransactionUseCase;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.domain.model.exception.ParentTransactionNotFoundException;
import com.mendel.service_transaction.domain.model.exception.TransactionAlreadyExistsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

	private final TransactionRepository transactionRepository;

	@Override
	public void createTransaction(CreateTransactionCommand command) {
		if (transactionRepository.findById(command.id()).isPresent()) {
			throw new TransactionAlreadyExistsException(command.id());
		}

		if (command.parentId() != null && transactionRepository.findById(command.parentId()).isEmpty()) {
			throw new ParentTransactionNotFoundException(command.parentId());
		}

		Transaction transaction = Transaction.builder()
										.id(command.id())
										.amount(new BigDecimal(command.amount()))
										.type(command.type())
										.parentId(command.parentId())
									.build();

		transactionRepository.save(transaction);
	}
}