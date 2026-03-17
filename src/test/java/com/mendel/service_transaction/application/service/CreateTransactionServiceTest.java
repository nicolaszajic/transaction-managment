package com.mendel.service_transaction.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.mendel.service_transaction.application.model.CreateTransactionCommand;
import com.mendel.service_transaction.application.repository.TransactionRepository;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.domain.model.exception.TransactionAlreadyExistsException;

public class CreateTransactionServiceTest {
	  @Test
	    void shouldCreateTransactionSuccessfullyWhenParentIdIsNull() {
	        TransactionRepository repository = new InMemoryTransactionRepositoryFake();
	        CreateTransactionService service = new CreateTransactionService(repository);

	        CreateTransactionCommand command = new CreateTransactionCommand(
	                1L,
	                100.0,
	                "cars",
	                null
	        );

	        service.createTransaction(command);

	        Optional<Transaction> savedTransaction = repository.findById(1L);

	        assertTrue(savedTransaction.isPresent());
	        assertEquals(1L, savedTransaction.get().getId());
	        assertEquals(new BigDecimal(100.0), savedTransaction.get().getAmount());
	        assertEquals("cars", savedTransaction.get().getType());
	        assertNull(savedTransaction.get().getParentId());
	    }
	  
		@Test
		  void shouldThrowExceptionWhenTransactionAlreadyExists() {
		      TransactionRepository repository = new InMemoryTransactionRepositoryFake();
		      CreateTransactionService service = new CreateTransactionService(repository);
	

		      repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));
	
		      CreateTransactionCommand command = new CreateTransactionCommand(
		              1L,
		              200.0,
		              "food",
		              null
		      );
	
		      assertThrows(
		              TransactionAlreadyExistsException.class,
		              () -> service.createTransaction(command)
		      );
		  }

	    private static class InMemoryTransactionRepositoryFake implements TransactionRepository {

	        private final List<Transaction> transactions = new ArrayList<>();

	        @Override
	        public void save(Transaction transaction) {
	            transactions.add(transaction);
	        }

	        @Override
	        public Optional<Transaction> findById(Long id) {
	            return transactions.stream()
	                    .filter(transaction -> transaction.getId().equals(id))
	                    .findFirst();
	        }

	        @Override
	        public List<Transaction> findByType(String type) {
	            return transactions.stream()
	                    .filter(transaction -> transaction.getType().equals(type))
	                    .toList();
	        }

	        @Override
	        public List<Transaction> findAll() {
	            return List.copyOf(transactions);
	        }
	    }
	}

