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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {

	private final TransactionRepository transactionRepository;

	@Override
	public void createTransaction(CreateTransactionCommand command) {
		log.info("Creating transaction with id={}, type={}, parentId={}",
                command.id(), command.type(), command.parentId());
		if (transactionRepository.findById(command.id()).isPresent()) {
			log.warn("Transaction with id={} already exists", command.id());
			throw new TransactionAlreadyExistsException(command.id());
		}

		if (command.parentId() != null && transactionRepository.findById(command.parentId()).isEmpty()) {
			log.warn("Parent transaction with id={} not found", command.parentId());
			throw new ParentTransactionNotFoundException(command.parentId());
		}

		Transaction transaction = Transaction.builder()
										.id(command.id())
										.amount(new BigDecimal(command.amount()))
										.type(command.type())
										.parentId(command.parentId())
									.build();

		transactionRepository.save(transaction);
		log.info("Transaction with id={} created successfully", command.id());
	}
}