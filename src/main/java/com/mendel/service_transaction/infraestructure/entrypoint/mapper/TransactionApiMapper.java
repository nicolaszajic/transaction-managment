package com.mendel.service_transaction.infraestructure.entrypoint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mendel.service_transaction.application.model.CreateTransactionCommand;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.CreateTransactionRequest;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.StatusResponse;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.SumResponse;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionApiMapper {

    @Mapping(target = "id", source = "transactionId")
    CreateTransactionCommand toCreateTransactionCommand(Long transactionId, CreateTransactionRequest request);

    default StatusResponse toStatusResponse(String status) {
        StatusResponse response = new StatusResponse();
        response.setStatus(status);
        return response;
    }

    default SumResponse toSumResponse(BigDecimal sum) {
        SumResponse response = new SumResponse();
        response.setSum(sum.doubleValue());
        return response;
    }
}
