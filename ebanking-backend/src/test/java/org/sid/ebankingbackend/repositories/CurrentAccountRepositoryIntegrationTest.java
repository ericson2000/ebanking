package org.sid.ebankingbackend.repositories;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
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
public class CurrentAccountRepositoryIntegrationTest {

    @Autowired
    private BankAccountRepository<CurrentAccount> currentAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer persistedCustomer;

    private

    @BeforeEach
    void createCustomer() {
        persistedCustomer = customerRepository.save(CustomerPopulator.createCustomer("eric", "eric@gmail.com"));
    }

    @AfterEach
    void setupDatabase() {
        this.currentAccountRepository.deleteAll();
    }

    @Test
    void given_currentAccount_when_save_then_return_currentAccount_persisted() {

        //GIVEN
        CurrentAccount currentAccount = BankAccountPopulator.createCurrentAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);

        //WHEN
        CurrentAccount persistedCurrentAccount = currentAccountRepository.save(currentAccount);

        // THEN
        Assertions.assertEquals(persistedCurrentAccount,currentAccount);
        Assertions.assertTrue(currentAccountRepository.findById(persistedCurrentAccount.getId()).isPresent());
    }

    @Test
    void given_currentAccount_persisted_when_findBydId_then_return_currentAccount_persisted() {

        //GIVEN
        CurrentAccount currentAccount = BankAccountPopulator.createCurrentAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);
        CurrentAccount persistedCurrentAccount = currentAccountRepository.save(currentAccount);
        //WHEN
        Optional<BankAccount> retrievedCurrentAccount = currentAccountRepository.findById(persistedCurrentAccount.getId());

        // THEN
        Assertions.assertTrue(retrievedCurrentAccount.isPresent());
    }

    @Test
    void given_currentAccount_persisted_when_update_then_return_currentAccount_updated() {

        //GIVEN
        CurrentAccount currentAccount = BankAccountPopulator.createCurrentAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);
        CurrentAccount persistedCurrentAccount = currentAccountRepository.save(currentAccount);
        persistedCurrentAccount.setBalance(2000);
        //WHEN
        CurrentAccount updatedCurrentAccount = currentAccountRepository.save(persistedCurrentAccount);

        // THEN
        Assertions.assertEquals(persistedCurrentAccount.getBalance(),updatedCurrentAccount.getBalance());
    }

    @Test
    void given_currentAccount_persisted_when_delete_then_return_currentAccount_is_not_present() {

        //GIVEN
        CurrentAccount currentAccount = BankAccountPopulator.createCurrentAccount(UUID.randomUUID().toString(),Math.random() * 900000, new Date(), AccountStatus.CREATED, persistedCustomer, 9000);
        CurrentAccount persistedCurrentAccount = currentAccountRepository.save(currentAccount);

        //WHEN
         currentAccountRepository.delete(persistedCurrentAccount);

        // THEN
        Assertions.assertTrue(currentAccountRepository.findById(persistedCurrentAccount.getId()).isEmpty());
    }
}
