package com.mendel.service_transaction.domain.model.exception;

public class TransactionAlreadyExistsException extends RuntimeException {

	public TransactionAlreadyExistsException(Long id) {
		super("Transaction already exists with id: " + id);
	}
}