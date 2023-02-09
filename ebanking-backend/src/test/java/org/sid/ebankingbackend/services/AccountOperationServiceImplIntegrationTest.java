package org.sid.ebankingbackend.services;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class})
@DirtiesContext
public class AccountOperationServiceImplIntegrationTest {

    @Autowired
    private AccountOperationService accountOperationService;

    @Autowired
    private AccountOperationRepository accountOperationRepository;

    @Autowired
    private  CurrentAccountService currentAccountService;

    @Autowired(required = false)
    private  SavingAccountService savingAccountService;

    @Autowired
    private CustomerRepository customerRepository;

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
        CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);

        //WHEN
        accountOperationService.debit(currentAccount.getId(), 60000,"achat produit intermart");

        //THEN
        final BankAccount retrievedCurrentAccount = currentAccountService.getBankAccount(currentAccount.getId());
        Assertions.assertEquals(30000,retrievedCurrentAccount.getBalance());
    }

    @Test
    void given_currentAccount_with_balance_when_credit_with_x_amount_then_the_balance_adding_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(5000, persistedCustomer.getId(), 20000);

        //WHEN
        accountOperationService.credit(currentAccount.getId(), 60000,"paiement du salaire");

        //THEN
        final BankAccount retrievedCurrentAccount = currentAccountService.getBankAccount(currentAccount.getId());
        Assertions.assertEquals(65000,retrievedCurrentAccount.getBalance());
    }


    @Test
    void given_currentAccount_with_insuffisant_balance_when_debit_with_x_amount_then_throw_BalanceNotSufficientException() throws CustomerNotFoundException{

        //GIVEN
        CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(3000, persistedCustomer.getId(), 20000);

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
        SavingAccount savingAccount = savingAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);

        //WHEN
        accountOperationService.debit(savingAccount.getId(), 2000,"achat produit intermart");

        //THEN
        final BankAccount retrievedSavingAccount = currentAccountService.getBankAccount(savingAccount.getId());
        Assertions.assertEquals(5000, retrievedSavingAccount.getBalance());
    }

    @Test
    void given_savingAccount_with_balance_when_credit_with_x_amount_then_the_balance_adding_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        SavingAccount savingAccount = savingAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);

        //WHEN
        accountOperationService.credit(savingAccount.getId(), 40000,"paiement du salaire");

        //THEN
        final BankAccount retrievedSavingAccount = currentAccountService.getBankAccount(savingAccount.getId());
        Assertions.assertEquals(47000, retrievedSavingAccount.getBalance());
    }

    @Test
    void given_savingAccount_with_insuffisant_balance_when_debit_with_x_amount_then_throw_BalanceNotSufficientException() throws CustomerNotFoundException {

        //GIVEN
        SavingAccount savingAccount = savingAccountService.saveSavingAccount(2000, persistedCustomer.getId(), 500);

        // WHEN
        Exception exception = Assertions.assertThrows(BalanceNotSufficientException.class,() -> {
            accountOperationService.debit(savingAccount.getId(), 6000,"achat produit intermart");;
        });

        //THEN
        Assertions.assertEquals("Balance not sufficient",exception.getMessage());
    }

    @Test
    void given_currentAccount_and_savingAccount_with_balance_when_transfert_from_currentAccout_to_savingAccount_with_x_amount_then_balance_of_currentAcc_reducing_to_x_and_balance_of_savingAcc_adding_to_x() throws CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        SavingAccount savingAccount = savingAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        //WHEN
        accountOperationService.transfert(currentAccount.getId(), savingAccount.getId(),50000);

        //THEN
        final BankAccount retrievedCurrentAccount = currentAccountService.getBankAccount(currentAccount.getId());
        final BankAccount retrievedSavingAccount = currentAccountService.getBankAccount(savingAccount.getId());

        Assertions.assertEquals(40000,retrievedCurrentAccount.getBalance());
        Assertions.assertEquals(57000,retrievedSavingAccount.getBalance());
    }
}
