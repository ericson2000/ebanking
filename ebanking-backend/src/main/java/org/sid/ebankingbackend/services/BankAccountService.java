package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;

import java.util.List;

public interface BankAccountService {

    BankAccount getBankAccount(String  accountId) throws BankAccountNotFoundException;

    List<BankAccount> getListBankAccount();

}
