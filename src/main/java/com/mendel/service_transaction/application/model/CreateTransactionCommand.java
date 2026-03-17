package com.mendel.service_transaction.application.model;

import java.math.BigDecimal;

public record CreateTransactionCommand(Long id, BigDecimal amount, String type, Long parentId) {
}