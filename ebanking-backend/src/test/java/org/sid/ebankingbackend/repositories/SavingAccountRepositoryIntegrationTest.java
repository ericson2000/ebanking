package org.sid.ebankingbackend.repositories;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.enums.AccountStatus;
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
public class SavingAccountRepositoryIntegrationTest {

    @Autowired
    private BankAccountRepository<SavingAccount> savingAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer persistedCustomer;

    private

    @BeforeEach()
    void createCustomer() {
        persistedCustomer = customerRepository.save(CustomerPopulator.createCustomer("eric", "eric@gmail.com"));
    }

    @AfterEach
    void setupDatabase() {
        this.savingAccountRepository.deleteAll();
    }

    @Test
    void given_savingAccount_when_save_then_return_savingAccount_persisted() {

        //GIVEN
        SavingAccount savingAccount = BankAccountPopulator.createSavingAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);

        //WHEN
        SavingAccount persistedSavingAccount = savingAccountRepository.save(savingAccount);

        // THEN
        Assertions.assertEquals(persistedSavingAccount,savingAccount);
        Assertions.assertEquals(savingAccountRepository.findAll().size(),1);
    }

    @Test
    void given_currentAccount_persisted_when_findBydId_then_return_currentAccount_persisted() {

        //GIVEN
        SavingAccount savingAccount = BankAccountPopulator.createSavingAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);
        SavingAccount persistedSavingAccount = savingAccountRepository.save(savingAccount);
        //WHEN
        Optional<BankAccount> retrievedSavingAccount = savingAccountRepository.findById(persistedSavingAccount.getId());

        // THEN
        Assertions.assertTrue(retrievedSavingAccount.isPresent());
    }

    @Test
    void given_currentAccount_persisted_when_update_then_return_currentAccount_updated() {

        //GIVEN
        SavingAccount savingAccount = BankAccountPopulator.createSavingAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);
        SavingAccount persistedSavingAccount = savingAccountRepository.save(savingAccount);
        persistedSavingAccount.setBalance(2000);
        //WHEN
        SavingAccount updatedSavingAccount = savingAccountRepository.save(persistedSavingAccount);

        // THEN
        Assertions.assertEquals(persistedSavingAccount.getBalance(), updatedSavingAccount.getBalance());
    }

    @Test
    void given_currentAccount_persisted_when_delete_then_return_currentAccount_is_not_present() {

        //GIVEN
        SavingAccount savingAccount = BankAccountPopulator.createSavingAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);
        SavingAccount persistedSavingAccount = savingAccountRepository.save(savingAccount);

        //WHEN
        savingAccountRepository.delete(persistedSavingAccount);

        // THEN
        Assertions.assertTrue(savingAccountRepository.findById(persistedSavingAccount.getId()).isEmpty());
    }
}
