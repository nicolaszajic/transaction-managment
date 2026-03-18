package com.mendel.service_transaction.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.application.usecase.GetTransactionsSumUseCase;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.domain.model.exception.TransactionNotFoundException;
@Slf4j
@Service
@RequiredArgsConstructor
public class GetTransactionSumService implements GetTransactionsSumUseCase {

	private final TransactionRepository transactionRepository;

	@Override
	public BigDecimal getSum(Long transactionId) {
		log.info("Calculating transitive sum for transactionId={}", transactionId);
		Transaction rootTransaction = transactionRepository
										.findById(transactionId)
										.orElseThrow(() -> {
											log.warn("Transaction with id={} not found for sum calculation", transactionId);
											return new TransactionNotFoundException(transactionId);
										});

        BigDecimal result = sumTransactionTree(rootTransaction, transactionRepository.findAll());
        log.info("Calculated sum for transactionId={} is {}", transactionId, result);

        return result;
	}

	private BigDecimal sumTransactionTree(Transaction root, List<Transaction> allTransactions) {
		BigDecimal total = root.getAmount() != null ? root.getAmount() : BigDecimal.ZERO;

		for (Transaction transaction : allTransactions) {
			if (root.getId().equals(transaction.getParentId())) {
				total = total.add(sumTransactionTree(transaction, allTransactions));
			}
		}

		return total;
	}
}