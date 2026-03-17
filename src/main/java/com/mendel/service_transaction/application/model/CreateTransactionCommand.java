package com.mendel.service_transaction.application.model;

public record CreateTransactionCommand(Long id, double amount, String type, Long parentId) {
}