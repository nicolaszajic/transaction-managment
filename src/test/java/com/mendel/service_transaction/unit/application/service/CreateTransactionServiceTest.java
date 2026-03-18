package com.mendel.service_transaction.unit.application.service;

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
import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.application.service.CreateTransactionService;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.domain.model.exception.ParentTransactionNotFoundException;
import com.mendel.service_transaction.domain.model.exception.TransactionAlreadyExistsException;
import com.mendel.service_transaction.unit.application.service.utils.InMemoryTransactionRepositoryMock;

public class CreateTransactionServiceTest {
	  @Test
	    void shouldCreateTransactionSuccessfullyWhenParentIdIsNull() {
	        TransactionRepository repository = new InMemoryTransactionRepositoryMock();
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
		      TransactionRepository repository = new InMemoryTransactionRepositoryMock();
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
		
		@Test
		  void shouldThrowExceptionWhenTransactionDoesNotExist() {
		      TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		      CreateTransactionService service = new CreateTransactionService(repository);
	
		      repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));
	
		      CreateTransactionCommand command = new CreateTransactionCommand(
		              2L,
		              200.0,
		              "clother",
		              999L
		      );
	
		      assertThrows(
		    		  ParentTransactionNotFoundException.class,
		              () -> service.createTransaction(command)
		      );
		  }
		
		@Test
		void shouldCreateTransactionWhenParentExists() {
		    TransactionRepository repository = new InMemoryTransactionRepositoryMock();
		    CreateTransactionService service = new CreateTransactionService(repository);

		    repository.save(new Transaction(1L, new BigDecimal(100.0), "cars", null));

		    CreateTransactionCommand command = new CreateTransactionCommand(
		            2L,
		            200.0,
		            "shopping",
		            1L
		    );

		    service.createTransaction(command);

		    Optional<Transaction> savedTransaction = repository.findById(2L);

		    assertTrue(savedTransaction.isPresent());
		    assertEquals(1L, savedTransaction.get().getParentId());
		}
	}

