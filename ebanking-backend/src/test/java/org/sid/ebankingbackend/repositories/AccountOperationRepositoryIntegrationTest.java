package org.sid.ebankingbackend.repositories;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.populator.AccountOperationPopulator;
import org.sid.ebankingbackend.populator.BankAccountPopulator;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class})
@DirtiesContext
//@DataJpaTest
public class AccountOperationRepositoryIntegrationTest {

    @Autowired
    private AccountOperationRepository<BankAccount> accountOperationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrentAccountRepository currentAccountRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    private CurrentAccount currentAccount;

    private SavingAccount savingAccount;

    private Customer customer;


    @BeforeEach
    void createBankAccount() {
        customer = CustomerPopulator.createCustomer("eric", "eric@gmail.com");
        customerRepository.save(customer);
        currentAccount = BankAccountPopulator.createCurrentAccount(UUID.randomUUID().toString(), Math.random() * 900000, new Date(), AccountStatus.CREATED, customer, 9000);
        currentAccountRepository.save(currentAccount);
        savingAccount = BankAccountPopulator.createSavingAccount(UUID.randomUUID().toString(), Math.random() * 900000, new Date(), AccountStatus.CREATED, customer, 9000);
        savingAccountRepository.save(savingAccount);
    }


    @AfterEach
    void setupDatabase() {
        this.accountOperationRepository.deleteAll();
    }

    @Test
    void given_accountOperation_on_savingAccount_when_save_the_return_accountOperation_persisted() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,savingAccount);

        //WHEN
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);

        // THEN
        Assertions.assertEquals(persistedAccountOperation,accountOperation);
        Assertions.assertTrue(accountOperationRepository.findById(persistedAccountOperation.getId()).isPresent());
    }

    @Test
    void given_accountOperation_on_currentAccount_when_save_the_return_accountOperation_persisted() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,currentAccount);

        //WHEN
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);

        // THEN
        Assertions.assertEquals(persistedAccountOperation,accountOperation);
        Assertions.assertTrue(accountOperationRepository.findById(persistedAccountOperation.getId()).isPresent());
    }

    @Test
    void given_accountOperation_on_currentAccount_persisted_when_findById_the_return_accountOperation_persisted() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,currentAccount);
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);

        //WHEN
        Optional<AccountOperation> retriedAccountOperation = accountOperationRepository.findById(persistedAccountOperation.getId());


        // THEN
        Assertions.assertTrue(retriedAccountOperation.isPresent());
    }

    @Test
    void given_accountOperation_on_savingAccount_persisted_when_findById_the_return_accountOperation_persisted() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,savingAccount);
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);

        //WHEN
        Optional<AccountOperation> retriedAccountOperation = accountOperationRepository.findById(persistedAccountOperation.getId());


        // THEN
        Assertions.assertTrue(retriedAccountOperation.isPresent());
    }

    @Test
    void given_accountOperation_on_savingAccount_persisted_when_updated_the_return_accountOperation_updated() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,savingAccount);
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);
        persistedAccountOperation.setAmount(9000);

        //WHEN
        AccountOperation updatedAccountOperation = accountOperationRepository.save(persistedAccountOperation);


        // THEN
        Assertions.assertEquals(persistedAccountOperation.getAmount(),updatedAccountOperation.getAmount());
    }

    @Test
    void given_accountOperation_on_currentAaccount_persisted_when_updated_the_return_accountOperation_updated() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,currentAccount);
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);
        persistedAccountOperation.setAmount(9000);

        //WHEN
        AccountOperation updatedAccountOperation = accountOperationRepository.save(persistedAccountOperation);


        // THEN
        Assertions.assertEquals(persistedAccountOperation.getAmount(),updatedAccountOperation.getAmount());
    }

    @Test
    void given_accountOperation_on_currentAccount_persisted_when_delete_the_return_accountOperation_is_not_present() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,currentAccount);
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);

        //WHEN
        accountOperationRepository.delete(persistedAccountOperation);

        // THEN
        Assertions.assertTrue(accountOperationRepository.findById(persistedAccountOperation.getId()).isEmpty());
    }

    @Test
    void given_accountOperation_on_savingAccount_persisted_when_delete_the_return_accountOperation_is_not_present() {

        //GIVEN
        AccountOperation accountOperation = AccountOperationPopulator.createAccountOperation(new Date(),8000, OperationType.CREDIT,savingAccount);
        AccountOperation persistedAccountOperation = accountOperationRepository.save(accountOperation);


        //WHEN
        accountOperationRepository.delete(persistedAccountOperation);


        // THEN
        Assertions.assertTrue(accountOperationRepository.findById(persistedAccountOperation.getId()).isEmpty());
    }
}
