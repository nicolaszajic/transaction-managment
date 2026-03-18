package com.mendel.service_transaction.unit.application.service;

import org.junit.jupiter.api.Test;

import com.mendel.service_transaction.application.model.CreateTransactionCommand;
import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.application.service.GetTransactionSumService;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.domain.model.exception.ParentTransactionNotFoundException;
import com.mendel.service_transaction.domain.model.exception.TransactionAlreadyExistsException;
import com.mendel.service_transaction.domain.model.exception.TransactionNotFoundException;
import com.mendel.service_transaction.unit.application.service.utils.InMemoryTransactionRepositoryMock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetTransactionSumServiceTest {

	@Test
	void shouldReturnSumForTransactionWithoutChildren() {
		TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		GetTransactionSumService service = new GetTransactionSumService(repository);

		repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));

		BigDecimal result = service.getSum(1L);

		assertEquals(new BigDecimal(100.0), result);
	}

	@Test
	void shouldReturnTransitiveSumIncludingChildrenAndGrandChildren() {
		TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		GetTransactionSumService service = new GetTransactionSumService(repository);

		repository.save(new Transaction(10L, new BigDecimal(5000.0), "cars", null));
		repository.save(new Transaction(11L, new BigDecimal(1000.0), "shopping", 10L));
		repository.save(new Transaction(12L, new BigDecimal(4000.0), "groceries", 11L));
		repository.save(new Transaction(13L, new BigDecimal(500.0), "travel", 10L));

		BigDecimal result = service.getSum(10L);

		assertEquals(new BigDecimal(10500.0), result);
	}

	@Test
	void shouldReturnTransitiveSumForNestedNode() {
		TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		GetTransactionSumService service = new GetTransactionSumService(repository);

		repository.save(new Transaction(10L, new BigDecimal(5000.0), "cars", null));
		repository.save(new Transaction(11L, new BigDecimal(1000.0), "shopping", 10L));
		repository.save(new Transaction(12L, new BigDecimal(4000.0), "groceries", 11L));

		BigDecimal result = service.getSum(11L);

		assertEquals(new BigDecimal(5000.0), result);
	}

	@Test
	void shouldThrowExceptionWhenTransactionDoesNotExist() {
		TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		GetTransactionSumService service = new GetTransactionSumService(repository);

		assertThrows(TransactionNotFoundException.class, () -> service.getSum(999L));
	}
}