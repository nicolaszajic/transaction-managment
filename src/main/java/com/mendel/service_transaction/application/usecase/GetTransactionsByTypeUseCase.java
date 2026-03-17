package com.mendel.service_transaction.application.usecase;

import java.util.List;

public interface GetTransactionsByTypeUseCase {
	List<Long> getByType(String type);
}