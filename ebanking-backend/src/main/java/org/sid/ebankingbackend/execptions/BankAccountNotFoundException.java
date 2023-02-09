package org.sid.ebankingbackend.execptions;

public class BankAccountNotFoundException extends Exception{

    public BankAccountNotFoundException(String message){
        super((message));
    }
}
