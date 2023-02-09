package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("savingAccountService")
public interface SavingAccountService extends BankAccountService{

    SavingAccount saveSavingAccount(double initialBalance, Long customerId, double interest) throws CustomerNotFoundException;

}
