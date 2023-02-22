package org.sid.ebankingbackend.services;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.dtos.AccountHistoryDto;
import org.sid.ebankingbackend.dtos.AccountOperationDto;
import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class})
@DirtiesContext
public class AccountOperationServiceImplIntegrationTest {

    @Autowired
    private AccountOperationService<BankAccount> accountOperationService;

    @Autowired
    private AccountOperationRepository<BankAccount> accountOperationRepository;

    @Autowired
    private  BankAccountService bankAccountService;

    @Autowired
    private CustomerRepository customerRepository ;

    private Customer customer;

    private Customer persistedCustomer;

    @BeforeEach
    void setUp() {
        customer = CustomerPopulator.createCustomer("eric","eric@gmail.com");
        persistedCustomer = customerRepository.save(customer);
    }

    @AfterEach
    void clear() {
        accountOperationRepository.deleteAll();
    }

    @Test
    void given_currentAccount_with_balance_when_debit_with_x_amount_then_the_balance_reducing_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);

        //WHEN
        accountOperationService.debit(currentAccount.getId(), 60000,"achat produit intermart");

        //THEN
        final BankAccountDto retrievedCurrentAccountDto = bankAccountService.getBankAccount(currentAccount.getId());
        Assertions.assertEquals(30000, retrievedCurrentAccountDto.getBalance());
    }

    @Test
    void given_currentAccount_with_balance_when_credit_with_x_amount_then_the_balance_adding_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(5000, persistedCustomer.getId(), 20000);

        //WHEN
        accountOperationService.credit(currentAccount.getId(), 60000,"paiement du salaire");

        //THEN
        final BankAccountDto retrievedCurrentAccountDto = bankAccountService.getBankAccount(currentAccount.getId());
        Assertions.assertEquals(65000, retrievedCurrentAccountDto.getBalance());
    }


    @Test
    void given_currentAccount_with_insuffisant_balance_when_debit_with_x_amount_then_throw_BalanceNotSufficientException() throws CustomerNotFoundException{

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(3000, persistedCustomer.getId(), 20000);

        // WHEN
        Exception exception = Assertions.assertThrows(BalanceNotSufficientException.class,() -> {
            accountOperationService.debit(currentAccount.getId(), 6000,"achat produit intermart");;
        });

        //THEN
        Assertions.assertEquals("Balance not sufficient",exception.getMessage());
    }

    @Test
    void given_savingAccount_with_balance_when_debit_with_x_amount_then_the_balance_reducing_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);

        //WHEN
        accountOperationService.debit(savingAccount.getId(), 2000,"achat produit intermart");

        //THEN
        final BankAccountDto retrievedSavingAccountDto = bankAccountService.getBankAccount(savingAccount.getId());
        Assertions.assertEquals(5000, retrievedSavingAccountDto.getBalance());
    }

    @Test
    void given_savingAccount_with_balance_when_credit_with_x_amount_then_the_balance_adding_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);

        //WHEN
        accountOperationService.credit(savingAccount.getId(), 40000,"paiement du salaire");

        //THEN
        final BankAccountDto retrievedSavingAccountDto = bankAccountService.getBankAccount(savingAccount.getId());
        Assertions.assertEquals(47000, retrievedSavingAccountDto.getBalance());
    }

    @Test
    void given_savingAccount_with_insuffisant_balance_when_debit_with_x_amount_then_throw_BalanceNotSufficientException() throws CustomerNotFoundException {

        //GIVEN
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(2000, persistedCustomer.getId(), 500);

        // WHEN
        Exception exception = Assertions.assertThrows(BalanceNotSufficientException.class,() -> {
            accountOperationService.debit(savingAccount.getId(), 6000,"achat produit intermart");
        });

        //THEN
        Assertions.assertEquals("Balance not sufficient",exception.getMessage());
    }

    @Test
    void given_currentAccount_and_savingAccount_with_balance_when_transfert_from_currentAccout_to_savingAccount_with_x_amount_then_balance_of_currentAcc_reducing_to_x_and_balance_of_savingAcc_adding_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        //WHEN
        accountOperationService.transfert(currentAccount.getId(), savingAccount.getId(),50000);

        //THEN
        final BankAccountDto retrievedCurrentAccountDto = bankAccountService.getBankAccount(currentAccount.getId());
        final BankAccountDto retrievedSavingAccountDto = bankAccountService.getBankAccount(savingAccount.getId());

        Assertions.assertEquals(40000,retrievedCurrentAccountDto.getBalance());
        Assertions.assertEquals(57000, retrievedSavingAccountDto.getBalance());
    }

    @Test
    void given_currentAccount_exist_with_some_debit_credit_and_transfert_operations_when_accountHistory_by_accountId_then_return_list_of_operations() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(5000, persistedCustomer.getId(), 20000);
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        accountOperationService.credit(currentAccount.getId(), 60000,"paiement du salaire");
        accountOperationService.debit(currentAccount.getId(), 6000,"achat produit intermart");
        accountOperationService.transfert(currentAccount.getId(), savingAccount.getId(),5000);

        //WHEN
        final List<AccountOperationDto> accountOperationDtos = accountOperationService.accountHistory(currentAccount.getId());

        //THEN;
        Assertions.assertEquals(3, accountOperationDtos.size());
    }

    @Test
    void given_currentAccount_exist_with_some_debit_credit_and_transfert_operations_when_getAccountHistory_by_accountId_page_size_then_return_AccountHistoryDto() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(5000, persistedCustomer.getId(), 20000);
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        accountOperationService.credit(currentAccount.getId(), 60000,"paiement du salaire");
        accountOperationService.debit(currentAccount.getId(), 6000,"achat produit intermart");
        accountOperationService.transfert(currentAccount.getId(), savingAccount.getId(),5000);

        //WHEN
        final AccountHistoryDto accountHistoryDto = accountOperationService.getAccountHistory(currentAccount.getId(),1,1);

        //THEN
        Assertions.assertEquals(54000, accountHistoryDto.getBalance());
    }
}
