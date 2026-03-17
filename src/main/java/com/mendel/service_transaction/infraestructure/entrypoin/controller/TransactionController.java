package com.mendel.service_transaction.infraestructure.entrypoin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;

import com.mendel.service_transaction.application.usecase.CreateTransactionUseCase;
import com.mendel.service_transaction.application.usecase.GetTransactionsByTypeUseCase;
import com.mendel.service_transaction.application.usecase.GetTransactionsSumUseCase;
import com.mendel.service_transaction.infraestructure.entrypoint.mapper.TransactionApiMapper;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.api.TransactionsApi;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.CreateTransactionRequest;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.StatusResponse;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.SumResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionsApi {

	private final CreateTransactionUseCase createTransactionUseCase;
	private final GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;
	private final GetTransactionsSumUseCase getTransactionSumUseCase;
	private final TransactionApiMapper transactionApiMapper;

	@Override
	public Mono<ResponseEntity<StatusResponse>> createTransaction(Long transactionId,
			Mono<CreateTransactionRequest> createTransactionRequest, ServerWebExchange exchange) {
		return createTransactionRequest.map(request -> {
			createTransactionUseCase
					.createTransaction(transactionApiMapper.toCreateTransactionCommand(transactionId, request));
			return ResponseEntity.ok(transactionApiMapper.toStatusResponse("ok"));
		});
	}

	@Override
	public Mono<ResponseEntity<SumResponse>> getTransactionSum(Long transactionId, ServerWebExchange exchange) {
		SumResponse response = transactionApiMapper.toSumResponse(getTransactionSumUseCase.getSum(transactionId));
		return Mono.just(ResponseEntity.ok(response));
	}

	@Override
	public Mono<ResponseEntity<Flux<Long>>> getTransactionsByType(String type, ServerWebExchange exchange) {
		Flux<Long> body = Flux.fromIterable(getTransactionsByTypeUseCase.getByType(type));
		return Mono.just(ResponseEntity.ok(body));
	}
}