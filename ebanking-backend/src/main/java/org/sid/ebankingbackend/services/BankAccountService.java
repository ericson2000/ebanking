package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    BankAccountDto getBankAccount(String  accountId) throws BankAccountNotFoundException;

    List<BankAccountDto> getListBankAccount();

    CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException;

    SavingAccount saveSavingAccount(double initialBalance, Long customerId, double interest) throws CustomerNotFoundException;

}
