package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class SavingAccountServiceImpl implements SavingAccountService {

    private BankAccountRepository<SavingAccount> savingAccountRepository;

    private CustomerRepository customerRepository;

    public SavingAccountServiceImpl(BankAccountRepository<SavingAccount> savingAccountRepository, CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
        this.savingAccountRepository = savingAccountRepository;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount = savingAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        return bankAccount;
    }

    @Override
    public List<BankAccount> getListBankAccount() {
        return savingAccountRepository.findAll();
    }

    @Override
    public SavingAccount saveSavingAccount(double initialBalance, Long customerId, double interest) throws CustomerNotFoundException {

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty())
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(optionalCustomer.get());
        savingAccount.setInterestRate(interest);

        SavingAccount savedSavingAccount = savingAccountRepository.save(savingAccount);

        return savedSavingAccount;
    }
}
