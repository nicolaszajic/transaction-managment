package com.mendel.service_transaction.infraestructure.entrypoint.adapter;

import org.springframework.stereotype.Repository;

import com.mendel.service_transaction.application.port.TransactionRepository;
import com.mendel.service_transaction.domain.model.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<Long, Transaction> transactionsById = new ConcurrentHashMap<>();
    private final Map<String, Set<Long>> transactionIdsByType = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> childrenIdsByParentId = new ConcurrentHashMap<>();
    
    @Override
    public void save(Transaction transaction) {
        transactionsById.put(transaction.getId(), transaction);
        
        transactionIdsByType
        .computeIfAbsent(transaction.getType(), key -> ConcurrentHashMap.newKeySet())
        .add(transaction.getId());

		if (transaction.getParentId() != null) {
		    childrenIdsByParentId
		            .computeIfAbsent(transaction.getParentId(), key -> ConcurrentHashMap.newKeySet())
		            .add(transaction.getId());
		}
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(transactionsById.get(id));
    }

    @Override
    public List<Transaction> findByType(String type) {
        Set<Long> ids = transactionIdsByType.getOrDefault(type, Collections.emptySet());

        return ids.stream()
                .map(transactionsById::get)
                .filter(Objects::nonNull)
                .toList();
    }
    
    @Override
    public List<Transaction> findChildrenByParentId(Long parentId) {
        Set<Long> ids = childrenIdsByParentId.getOrDefault(parentId, Collections.emptySet());

        return ids.stream()
                .map(transactionsById::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactionsById.values());
    }
    
    public void clear() {
        transactionsById.clear();
        transactionIdsByType.clear();
        childrenIdsByParentId.clear();
    }
}