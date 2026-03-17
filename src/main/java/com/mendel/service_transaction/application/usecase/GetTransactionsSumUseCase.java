package com.mendel.service_transaction.application.usecase;

import java.math.BigDecimal;

public interface GetTransactionsSumUseCase {
	BigDecimal getSum(Long id);
}
