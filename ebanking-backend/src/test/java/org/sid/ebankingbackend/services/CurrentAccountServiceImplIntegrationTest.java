package org.sid.ebankingbackend.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.sid.ebankingbackend.repositories.CurrentAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class})
@DirtiesContext
public class CurrentAccountServiceImplIntegrationTest {

    @Autowired
    private CurrentAccountService currentAccountService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrentAccountRepository currentAccountRepository;


    private Customer customer;

    @BeforeEach
    void setUp(){
        customer = CustomerPopulator.createCustomer("eric","eric@gmail.com");
        customerRepository.save(customer);
    }

    @AfterEach
    void deleteAll(){
        currentAccountRepository.deleteAll();
    }

    @Test
    void given_currentAccount_when_saveCurrentAccount_then_return_currentAccount_persisted() throws CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;

        // WHEN
        final CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);

        //THEN
        Assertions.assertEquals(90000,currentAccount.getBalance());
        Assertions.assertEquals(20000,currentAccount.getOverDraft());
    }

    @Test
    void given_currentAccount_with_customerId_not_exist_when_saveCurrentAccount_then_throw_customerNotFoundException() {
        //GIVEN

        // WHEN
        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class,() -> {
            currentAccountService.saveCurrentAccount(90000, 5L, 20000);
        });

        //THEN
        Assertions.assertEquals("Customer not found",exception.getMessage());
    }

    @Test
    void given_currentAccount_persisted_when_getBanAccount_then_return_currentAccount_persisted() throws BankAccountNotFoundException, CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;
        CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);


        // WHEN
        final BankAccountDto retrievedCurrentAccountDto = bankAccountService.getBankAccount(currentAccount.getId());

        //THEN
        Assertions.assertTrue(retrievedCurrentAccountDto instanceof CurrentAccountDto);
        Assertions.assertEquals(currentAccount.getId(), retrievedCurrentAccountDto.getId());
        Assertions.assertEquals(90000, retrievedCurrentAccountDto.getBalance());
    }

    @Test
    void given_currentAccount_not_persisted_when_getBanAccount_then_thorw_BankAccountNotFoundException() throws  CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;
        CurrentAccount currentAccount = currentAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);


        // WHEN
        Exception exception = Assertions.assertThrows(BankAccountNotFoundException.class,() -> {
            bankAccountService.getBankAccount(currentAccount.getId() + "unknown");
        });

        //THEN
        Assertions.assertEquals("BankAccount not found",exception.getMessage());
    }

    @Test
    void given_2_currentAccount_when_getListBankAccount_then_return_list_of_account() throws CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;
        currentAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 500);
        Customer secondCustomer = customerRepository.save(CustomerPopulator.createCustomer("erica","erica@gmail.com"));
        currentAccountService.saveCurrentAccount(7000, secondCustomer.getId(), 500);

        // WHEN
        final List<BankAccountDto> savingAccountDtos = bankAccountService.getListBankAccount();

        //THEN
        Assertions.assertEquals(2, savingAccountDtos.size());
    }

}
