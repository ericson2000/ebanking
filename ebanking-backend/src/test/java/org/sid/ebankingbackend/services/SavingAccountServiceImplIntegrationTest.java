package org.sid.ebankingbackend.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.repositories.SavingAccountRepository;
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
public class SavingAccountServiceImplIntegrationTest {

    @Autowired
    private SavingAccountService savingAccountService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    private Customer customer;

    @BeforeEach
    void setUp(){
        customer = CustomerPopulator.createCustomer("eric","eric@gmail.com");
        customerRepository.save(customer);
    }

    @AfterEach
    void deleteAll(){
        savingAccountRepository.deleteAll();
    }

    @Test
    void given_savingAccount_when_saveSavingAccount_then_return_savingAccount_persisted() throws CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;

        // WHEN
        final SavingAccount savingAccount = savingAccountService.saveSavingAccount(90000, persistedCustomer.getId(), 500);

        //THEN
        Assertions.assertEquals(90000,savingAccount.getBalance());
        Assertions.assertEquals(500,savingAccount.getInterestRate());
    }

    @Test
    void given_savingAccount_with_customerId_not_exist_when_saveSavingAccount_then_throw_customerNotFoundException() {
        //GIVEN

        // WHEN
        Exception exception = Assertions.assertThrows(CustomerNotFoundException.class,() -> {
            savingAccountService.saveSavingAccount(90000, 87l, 500);
        });

        //THEN
        Assertions.assertEquals("Customer not found",exception.getMessage());
    }

    @Test
    void given_savingAccount_persisted_when_getBanAccount_then_return_savingAccount_persisted() throws BankAccountNotFoundException, CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;
        SavingAccount savingAccount = savingAccountService.saveSavingAccount(90000, persistedCustomer.getId(), 20000);


        // WHEN
        final BankAccountDto retrievedSavingAccountDto = bankAccountService.getBankAccount(savingAccount.getId());

        //THEN
        Assertions.assertTrue(retrievedSavingAccountDto instanceof SavingAccountDto);
        Assertions.assertEquals(savingAccount.getId(), retrievedSavingAccountDto.getId());
        Assertions.assertEquals(90000, retrievedSavingAccountDto.getBalance());
    }

    @Test
    void given_savingAccount_not_persisted_when_getBanAccount_then_thorw_BankAccountNotFoundException() throws  CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;
        SavingAccount savingAccount = savingAccountService.saveSavingAccount(90000, persistedCustomer.getId(), 20000);

        // WHEN
        Exception exception = Assertions.assertThrows(BankAccountNotFoundException.class,() -> {
            bankAccountService.getBankAccount(savingAccount.getId()+ "unknown");
        });

        //THEN
        Assertions.assertEquals("BankAccount not found",exception.getMessage());
    }

    @Test
    void given_2_savingAccount_when_getListBankAccount_then_return_list_of_account() throws CustomerNotFoundException {
        //GIVEN
        Customer persistedCustomer = customer;
        savingAccountService.saveSavingAccount(90000, persistedCustomer.getId(), 500);
        Customer secondCustomer = customerRepository.save(CustomerPopulator.createCustomer("erica","erica@gmail.com"));
        savingAccountService.saveSavingAccount(7000, secondCustomer.getId(), 500);

        // WHEN
        final List<BankAccountDto> savingAccountDtos = bankAccountService.getListBankAccount();

        //THEN
        Assertions.assertEquals(2, savingAccountDtos.size());
    }
}
