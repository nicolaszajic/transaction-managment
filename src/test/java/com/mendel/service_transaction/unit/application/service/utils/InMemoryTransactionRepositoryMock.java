package com.mendel.service_transaction.unit.application.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.domain.model.Transaction;

public class InMemoryTransactionRepositoryMock implements TransactionRepository {

	private final List<Transaction> transactions = new ArrayList<>();

	@Override
	public void save(Transaction transaction) {
		transactions.add(transaction);
	}

	@Override
	public Optional<Transaction> findById(Long id) {
		return transactions.stream().filter(transaction -> transaction.getId().equals(id)).findFirst();
	}

	@Override
	public List<Transaction> findByType(String type) {
		return transactions.stream().filter(transaction -> transaction.getType().equals(type)).toList();
	}

	@Override
	public List<Transaction> findAll() {
		return List.copyOf(transactions);
	}
	
	@Override
	public List<Transaction> findChildrenByParentId(Long parentId) {
		 return transactions.stream()
                    .filter(transaction -> parentId.equals(transaction.getParentId()))
                    .toList();
	}
}