package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.repositories.BankAccountRepository;

import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
//@RequiredArgsConstructor
@Slf4j
@Primary
public class CurrentAccountServiceImpl implements CurrentAccountService {

    private BankAccountRepository<CurrentAccount> currentAccountRepository;

    private CustomerRepository customerRepository;

    public CurrentAccountServiceImpl(BankAccountRepository<CurrentAccount> currentAccountRepository,CustomerRepository customerRepository){
        this.currentAccountRepository = currentAccountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount = currentAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        return bankAccount;
    }

    @Override
    public List<BankAccount> getListBankAccount() {
        return currentAccountRepository.findAll();
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty())
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(optionalCustomer.get());
        currentAccount.setOverDraft(overdraft);

        CurrentAccount savedCurrentAccount = currentAccountRepository.save(currentAccount);

        return savedCurrentAccount;
    }
}
