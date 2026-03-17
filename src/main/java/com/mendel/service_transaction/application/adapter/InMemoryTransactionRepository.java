package com.mendel.service_transaction.application.adapter;

import org.springframework.stereotype.Repository;

import com.mendel.service_transaction.domain.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<Long, Transaction> transactionsById = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        transactionsById.put(transaction.getId(), transaction);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(transactionsById.get(id));
    }

    @Override
    public List<Transaction> findByType(String type) {
        return transactionsById.values().stream()
                .filter(transaction -> transaction.getType().equals(type))
                .toList();
    }

    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactionsById.values());
    }
}