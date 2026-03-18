package com.mendel.service_transaction.unit.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.application.service.GetTransactionsByTypeService;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.unit.application.service.utils.InMemoryTransactionRepositoryMock;

class GetTransactionsByTypeServiceTest {

	@Test
	void shouldReturnTransactionIdsByType() {
		TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		GetTransactionsByTypeService service = new GetTransactionsByTypeService(repository);

		repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));
		repository.save(new Transaction(2L, new BigDecimal(200.0), "clother", null));
		repository.save(new Transaction(3L, new BigDecimal(300.0), "cars", 1L));

		List<Long> result = service.getByType("cars");

		assertEquals(List.of(1L, 3L), result);
	}

	@Test
	void shouldReturnEmptyListWhenNoTransactionsMatchType() {
		TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		GetTransactionsByTypeService service = new GetTransactionsByTypeService(repository);

		repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));
		repository.save(new Transaction(2L, new BigDecimal(200.0), "clother", null));

		List<Long> result = service.getByType("travel");

		assertEquals(List.of(), result);
	}

	
}