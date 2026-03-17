package com.mendel.service_transaction.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public record Transaction(Long id, BigDecimal amount, String type, Long parentId) {

}