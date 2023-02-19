package org.sid.ebankingbackend.services;

import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CurrentAccountMapper;
import org.sid.ebankingbackend.mappers.SavingAccountMapper;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class BankAccountServiceImpl implements BankAccountService {

    protected  BankAccountRepository<BankAccount> bankAccountRepository;

    protected  CustomerRepository customerRepository;

    protected BankAccountServiceImpl(BankAccountRepository<BankAccount> currentAccountRepository, CustomerRepository customerRepository) {
        this.bankAccountRepository = currentAccountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (isCurrentAccount(bankAccount)) {
            return CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto((CurrentAccount) bankAccount);
        } else if (isSavingAccount(bankAccount)) {
            return SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto((SavingAccount) bankAccount);
        }
        return null;
    }

    @Override
    public List<BankAccountDto> getListBankAccount() {
        return  bankAccountRepository.findAll().stream().map(bankAccount -> {
            BankAccountDto bankAccountDto = null ;
            if (isCurrentAccount(bankAccount)){
                bankAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto((CurrentAccount) bankAccount);
            }else if (isSavingAccount(bankAccount)){
                bankAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto((SavingAccount) bankAccount);
            }
            return bankAccountDto;
        }).collect(Collectors.toList());
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

        return  bankAccountRepository.save(currentAccount);
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

        return bankAccountRepository.save(savingAccount);
    }

    private boolean isCurrentAccount(BankAccount bankAccount) {
        return bankAccount instanceof CurrentAccount;
    }

    private boolean isSavingAccount(BankAccount bankAccount) {
        return bankAccount instanceof SavingAccount;
    }
}
