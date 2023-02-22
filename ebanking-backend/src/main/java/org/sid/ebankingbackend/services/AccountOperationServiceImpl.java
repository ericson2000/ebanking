package org.sid.ebankingbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.AccountHistoryDto;
import org.sid.ebankingbackend.dtos.AccountOperationDto;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.mappers.AccountOperationMapper;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;

import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AccountOperationServiceImpl implements AccountOperationService<BankAccount> {

    private final AccountOperationRepository<BankAccount> accountOperationRepository;

    private final BankAccountRepository<BankAccount> bankAccountRepository;

    private final BankAccountService bankAccountService;


    public AccountOperationServiceImpl(AccountOperationRepository<BankAccount> accountOperationRepository, BankAccountRepository<BankAccount> bankAccountRepository, @Qualifier("savingAccountServiceImpl") BankAccountService bankAccountService) {
        this.accountOperationRepository = accountOperationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
    }


    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        AccountOperation accountOperation = new AccountOperation();
        setCommonAccountOperations(accountOperation, OperationType.DEBIT, amount, description, new Date());
        setCommonAccountAndBankOperations(accountOperation, bankAccount, bankAccount.getBalance() - amount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        setCommonAccountOperations(accountOperation, OperationType.CREDIT, amount, description, new Date());
        setCommonAccountAndBankOperations(accountOperation, bankAccount, bankAccount.getBalance() + amount);

    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

        debit(accountIdSource, amount, "Transfert to " + accountIdDestination);

        credit(accountIdDestination, amount, "Transfert from " + accountIdSource);
    }

    @Override
    public List<AccountOperationDto> accountHistory(String accountId) {

        return accountOperationRepository.findByBankAccountId(accountId)
                .stream()
                .map(AccountOperationMapper.INSTANCE::accountOperationToAccountOperationDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (Objects.isNull(bankAccount))
            throw new BankAccountNotFoundException("Account not found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));

        return AccountHistoryDto.builder()
                .accountOperationDtos(accountOperations.getContent().
                        stream().map(AccountOperationMapper.INSTANCE::accountOperationToAccountOperationDto).collect(Collectors.toList()))
                .accountId(bankAccount.getId())
                .balance(bankAccount.getBalance())
                .currentPage(page)
                .pageSize(size).totalPage(accountOperations.getTotalPages()).build();

    }


    private void setCommonAccountOperations(AccountOperation accountOperation, OperationType operationType, double amount, String description, Date date) {
        accountOperation.setType(operationType);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(date);
    }

    private void setCommonAccountAndBankOperations(AccountOperation accountOperation, BankAccount bankAccount, double balance) {
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(balance);
        bankAccountRepository.save(bankAccount);
    }


}
