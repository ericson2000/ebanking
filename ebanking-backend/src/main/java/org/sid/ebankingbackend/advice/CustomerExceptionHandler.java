package org.sid.ebankingbackend.advice;

import org.sid.ebankingbackend.execptions.ApiError;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleException(CustomerNotFoundException exception){

        ApiError apiError = new ApiError(UUID.randomUUID().toString(),exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return  new ResponseEntity<>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
