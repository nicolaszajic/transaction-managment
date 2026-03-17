package com.mendel.service_transaction.application.port;

import java.util.List;
import java.util.Optional;

import com.mendel.service_transaction.domain.model.Transaction;

public interface TransactionRepository {
	void save(Transaction transaction);
	Optional<Transaction> findById(Long id);
	List<Transaction> findByType(String type);
	List<Transaction> findAll();
}
