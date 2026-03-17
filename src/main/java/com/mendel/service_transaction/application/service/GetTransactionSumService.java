package com.mendel.service_transaction.application.service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.mendel.service_transaction.application.adapter.TransactionRepository;
import com.mendel.service_transaction.application.usecase.GetTransactionsSumUseCase;
import com.mendel.service_transaction.domain.model.Transaction;
import com.mendel.service_transaction.domain.model.exception.TransactionNotFoundException;

@RequiredArgsConstructor
public class GetTransactionSumService implements GetTransactionsSumUseCase {

	private final TransactionRepository transactionRepository;

	@Override
	public BigDecimal getSum(Long transactionId) {
		Transaction rootTransaction = transactionRepository
										.findById(transactionId)
										.orElseThrow(() -> new TransactionNotFoundException(transactionId));

		return sumTransactionTree(rootTransaction, transactionRepository.findAll());
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