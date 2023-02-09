package org.sid.ebankingbackend.populator;

import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;

public class BankAccountPopulator {

    public static CurrentAccount createCurrentAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer, double overDraft) {
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setOverDraft(overDraft);
        currentAccount.setBalance(balance);
        currentAccount.setCreatedAt(createdAt);
        currentAccount.setCustomer(customer);
        currentAccount.setStatus(status);
        currentAccount.setId(id);
        return currentAccount;
    }

    public static SavingAccount createSavingAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer, double interestRate) {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setInterestRate(interestRate);
        savingAccount.setBalance(balance);
        savingAccount.setCreatedAt(createdAt);
        savingAccount.setCustomer(customer);
        savingAccount.setStatus(status);
        savingAccount.setId(id);
        return savingAccount;
    }
}
