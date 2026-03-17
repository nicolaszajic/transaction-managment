package com.mendel.service_transaction.infraestructure.entrypoin.controller.exception;

import com.mendel.service_transaction.domain.model.exception.ParentTransactionNotFoundException;
import com.mendel.service_transaction.domain.model.exception.TransactionAlreadyExistsException;
import com.mendel.service_transaction.domain.model.exception.TransactionNotFoundException;
import com.mendel.service_transaction.infrastructure.entrypoint.controller.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTransactionAlreadyExists(
            TransactionAlreadyExistsException ex
    ) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ParentTransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParentNotFound(
            ParentTransactionNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFound(
            TransactionNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage("Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}