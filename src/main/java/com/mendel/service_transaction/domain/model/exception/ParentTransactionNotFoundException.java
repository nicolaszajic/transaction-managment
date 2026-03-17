package com.mendel.service_transaction.domain.model.exception;

public class ParentTransactionNotFoundException extends RuntimeException {

    public ParentTransactionNotFoundException(Long parentId) {
        super("Parent transaction not found with id: " + parentId);
    }
}