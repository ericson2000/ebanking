package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;

public interface AccountOperationService<T extends BankAccount> {

    void debit(String accountId, double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount,String description) throws BankAccountNotFoundException;

    void transfert(String accountIdSource, String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
