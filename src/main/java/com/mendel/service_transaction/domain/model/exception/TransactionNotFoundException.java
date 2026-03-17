package com.mendel.service_transaction.domain.model.exception;

public class TransactionNotFoundException extends RuntimeException {

	public TransactionNotFoundException(Long id) {
		super("Transaction not found with id: " + id);
	}
}