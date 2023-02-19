package org.sid.ebankingbackend.services;

import lombok.extern.slf4j.Slf4j;

import org.sid.ebankingbackend.entities.BankAccount;

import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class SavingAccountServiceImpl extends BankAccountServiceImpl implements SavingAccountService {

    public SavingAccountServiceImpl(BankAccountRepository<BankAccount> bankAccountRepository,CustomerRepository customerRepository){
        super( bankAccountRepository,customerRepository);
    }
}
