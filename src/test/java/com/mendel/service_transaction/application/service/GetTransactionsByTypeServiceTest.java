package com.mendel.service_transaction.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.mendel.service_transaction.application.adapter.TransactionRepository;
import com.mendel.service_transaction.domain.model.Transaction;

class GetTransactionsByTypeServiceTest {

	@Test
	void shouldReturnTransactionIdsByType() {
		TransactionRepository repository = new InMemoryTransactionRepositoryFake();
		GetTransactionsByTypeService service = new GetTransactionsByTypeService(repository);

		repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));
		repository.save(new Transaction(2L, new BigDecimal(200.0), "clother", null));
		repository.save(new Transaction(3L, new BigDecimal(300.0), "cars", 1L));

		List<Long> result = service.getByType("cars");

		assertEquals(List.of(1L, 3L), result);
	}

	@Test
	void shouldReturnEmptyListWhenNoTransactionsMatchType() {
		TransactionRepository repository = new InMemoryTransactionRepositoryFake();
		GetTransactionsByTypeService service = new GetTransactionsByTypeService(repository);

		repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));
		repository.save(new Transaction(2L, new BigDecimal(200.0), "clother", null));

		List<Long> result = service.getByType("travel");

		assertEquals(List.of(), result);
	}

	private static class InMemoryTransactionRepositoryFake implements TransactionRepository {

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
	}
}