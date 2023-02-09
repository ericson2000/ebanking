package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;

import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
//@RequiredArgsConstructor
@Slf4j
public class AccountOperationServiceImpl implements AccountOperationService {

    private AccountOperationRepository accountOperationRepository;

    private BankAccountRepository<?> bankAccountRepository;

    private BankAccountService bankAccountService;

    public AccountOperationServiceImpl(AccountOperationRepository accountOperationRepository, BankAccountRepository<?> bankAccountRepository, BankAccountService bankAccountService) {
        this.accountOperationRepository = accountOperationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
    }


    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = bankAccountService.getBankAccount(accountId);

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountService.getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

        debit(accountIdSource, amount, "Transfert to " + accountIdDestination);

        credit(accountIdDestination, amount, "Transfert from " + accountIdSource);
    }
}
