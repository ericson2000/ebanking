package org.sid.ebankingbackend.execptions;

public class BalanceNotSufficientException extends Exception{

    public BalanceNotSufficientException(String message){
        super(message);
    }
}
