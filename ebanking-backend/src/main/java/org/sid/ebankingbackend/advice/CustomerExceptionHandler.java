package org.sid.ebankingbackend.advice;

import org.sid.ebankingbackend.execptions.ApiError;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleException(CustomerNotFoundException exception){

        String error = exception.getMessage();
        return  buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,error,exception));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
