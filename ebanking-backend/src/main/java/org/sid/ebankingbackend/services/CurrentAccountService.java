package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;

public interface CurrentAccountService extends BankAccountService {

    CurrentAccount saveCurrentAccount(double initialBalance, Long customerId,double overdraft) throws CustomerNotFoundException;

}
