package org.sid.ebankingbackend.advice;

import org.sid.ebankingbackend.execptions.ApiError;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class BankAccountExceptionHandler {

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<Object> handleException(BankAccountNotFoundException exception){

        String error = exception.getMessage();
        return  buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,error,exception));
    }

    @ExceptionHandler(BalanceNotSufficientException.class)
    public ResponseEntity<Object> handleException(BalanceNotSufficientException exception){

        String error = exception.getMessage();
        return  buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,error,exception));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
