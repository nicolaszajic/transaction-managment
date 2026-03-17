package com.mendel.service_transaction.application.service;

import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.application.usecase.GetTransactionsByTypeUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetTransactionsByTypeService implements GetTransactionsByTypeUseCase {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Long> getByType(String type) {
        return transactionRepository.findByType(type)
                .stream()
                .map(transaction -> transaction.getId())
                .toList();
    }
}