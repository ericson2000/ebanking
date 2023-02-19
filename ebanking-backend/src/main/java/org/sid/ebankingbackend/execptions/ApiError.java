package org.sid.ebankingbackend.execptions;

import org.springframework.http.HttpStatus;

public class ApiError {

    private String id;
    private HttpStatus status;
    private String message;


    public ApiError(String id,String message,HttpStatus status) {
        super();
        this.id = id;
        this.status = status;
        this.message = message;
    }
}
