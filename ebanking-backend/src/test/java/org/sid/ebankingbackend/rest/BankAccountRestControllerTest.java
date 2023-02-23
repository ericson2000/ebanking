package org.sid.ebankingbackend.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.execptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.execptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.execptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.CurrentAccountMapper;
import org.sid.ebankingbackend.mappers.CustomerMapper;
import org.sid.ebankingbackend.mappers.SavingAccountMapper;
import org.sid.ebankingbackend.populator.BankAccountPopulator;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.AccountOperationService;
import org.sid.ebankingbackend.services.BankAccountService;
import org.sid.ebankingbackend.wiremock.WireMockConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class, WireMockConfig.class})
@DirtiesContext
public class BankAccountRestControllerTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountRepository<BankAccount> bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountOperationRepository<BankAccount> accountOperationRepository;

    @Autowired
    private AccountOperationService<BankAccount> accountOperationService;

    private Customer customer;

    private Customer persistedCustomer;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        WireMock.configureFor("localhost", 7070);
        customer = CustomerPopulator.createCustomer("eric", "eric@gmail.com");
        persistedCustomer = customerRepository.save(customer);
    }

    @AfterEach
    void deleteAll() {
        accountOperationRepository.deleteAll();
        bankAccountRepository.deleteAll();
        customerRepository.deleteAll();
        wireMockServer.resetAll();
    }

    @Test
    void given_currentAccount_exist_when_request_get_accounts_id_executed_then_return_currentAccount() throws IOException, CustomerNotFoundException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/accounts/" + currentAccount.getId()))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(currentAccountDto))));


        HttpUriRequest request = new HttpGet("http://localhost:7070/api/accounts/" + currentAccount.getId());

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/accounts/" + currentAccount.getId())));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(currentAccountDto));

    }

    @Test
    void given_savingAccount_exist_when_request_get_accounts_id_executed_then_return_savingAccount() throws IOException, CustomerNotFoundException {

        //GIVEN
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/accounts/" + savingAccount.getId()))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(savingAccountDto))));


        HttpUriRequest request = new HttpGet("http://localhost:7070/api/accounts/" + savingAccount.getId());

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/accounts/" + savingAccount.getId())));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(savingAccountDto));

    }

    @Test
    void given_savingAccount_and_currentAccount_exist_when_request_get_accounts_executed_then_return_2_bankAccounts() throws IOException, CustomerNotFoundException {

        //GIVEN
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/accounts/"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(Arrays.asList(savingAccountDto, currentAccountDto)))));


        HttpUriRequest request = new HttpGet("http://localhost:7070/api/accounts/");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/accounts/")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(Arrays.asList(savingAccountDto, currentAccountDto)));

    }

    @Test
    void given_currentAccount_exist_with_some_operations_debit_and_credit_when_request_getHistory_by_accountId_executed_then_return_list_of_operations() throws IOException, CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        accountOperationService.debit(currentAccount.getId(), 600, "achat produit intermart");
        accountOperationService.credit(currentAccount.getId(), 60000, "paiement du salaire");


        //WHEN
        List<AccountOperationDto> accountOperationDtos = accountOperationService.accountHistory(currentAccount.getId());

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/accounts/" + currentAccount.getId() + "/operations"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(accountOperationDtos))));


        HttpUriRequest request = new HttpGet("http://localhost:7070/api/accounts/" + currentAccount.getId() + "/operations");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/accounts/" + currentAccount.getId() + "/operations")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(accountOperationDtos));
    }

    @Test
    void given_currentAccount_exist_with_some_operations_debit_and_credit_when_request_getAccountHistory_by_accountId_page_size_then_return_AccountHistoryDto() throws IOException, CustomerNotFoundException, BankAccountNotFoundException, BalanceNotSufficientException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        accountOperationService.debit(currentAccount.getId(), 600, "achat produit intermart");
        accountOperationService.credit(currentAccount.getId(), 60000, "paiement du salaire");


        //WHEN
        AccountHistoryDto accountHistoryDto = accountOperationService.getAccountHistory(currentAccount.getId(), 1, 1);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/accounts/" + currentAccount.getId() + "/pageOperations?page=1&size=1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(accountHistoryDto))));


        HttpUriRequest request = new HttpGet("http://localhost:7070/api/accounts/" + currentAccount.getId() + "/pageOperations?page=1&size=1");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/accounts/" + currentAccount.getId() + "/pageOperations?page=1&size=1")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(accountHistoryDto));
    }

    @Test
    void given_currentAccount_exist_when_request_post_debit_then_debit_balance_account_and_return_status_200() throws IOException, CustomerNotFoundException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //WHEN
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/accounts/debit/" + currentAccount.getId() + "/2000/achats"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));


        HttpPost request = new HttpPost("http://localhost:7070/api/accounts/debit/" + currentAccount.getId() + "/2000/achats");
//        StringEntity entity = new StringEntity(getJsonAsString(currentAccountDto));
//        request.addHeader("Content-Type", "application/json");
//        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/api/accounts/debit/" + currentAccount.getId() + "/2000/achats")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
    }

    @Test
    void given_currentAccount_exist_when_request_post_credit_then_credit_balance_account_and_return_status_200() throws IOException, CustomerNotFoundException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);

        //WHEN
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/accounts/credit/" + currentAccount.getId() + "/2000/salaire"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));


        HttpPost request = new HttpPost("http://localhost:7070/api/accounts/credit/" + currentAccount.getId() + "/2000/salaire");
//        StringEntity entity = new StringEntity(getJsonAsString(currentAccountDto));
//        request.addHeader("Content-Type", "application/json");
//        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/api/accounts/credit/" + currentAccount.getId() + "/2000/salaire")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
    }

    @Test
    void given_currentAccount_and_savingAccount_exists_when_request_post_transfert_then_return_status_200() throws IOException, CustomerNotFoundException {

        //GIVEN
        CurrentAccount currentAccount = bankAccountService.saveCurrentAccount(90000, persistedCustomer.getId(), 20000);
        SavingAccount savingAccount = bankAccountService.saveSavingAccount(7000, persistedCustomer.getId(), 500);
        CurrentAccountDto currentAccountDto = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(currentAccount);
        SavingAccountDto savingAccountDto = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(savingAccount);

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/accounts/transfert/" + currentAccountDto.getId() + "/" + savingAccountDto.getId() + "/2000"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));


        HttpPost request = new HttpPost("http://localhost:7070/api/accounts/transfert/" + currentAccountDto.getId() + "/" + savingAccountDto.getId() + "/2000");
//        StringEntity entity = new StringEntity(getJsonAsString(currentAccountDto));
//        request.addHeader("Content-Type", "application/json");
//        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/api/accounts/transfert/" + currentAccountDto.getId() + "/" + savingAccountDto.getId() + "/2000")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());

    }

    @Test
    void given_currentAccountDto_not_exists_when_request_saveCurrentAccount_then_return_currentAccountDto_with_status_200() throws IOException, CustomerNotFoundException {

        //GIVEN
        CurrentAccountDto currentAccountDto = BankAccountPopulator.createCurrentAccountDto(UUID.randomUUID().toString(), Math.random() * 900000, new Date(), AccountStatus.CREATED, CustomerMapper.INSTANCE.customerToCustomerDto(persistedCustomer), 9000);
        BankAccountDto currentAccountDtoSaved = CurrentAccountMapper.INSTANCE.currentAccountToCurrentAccountDto(bankAccountService.saveCurrentAccount(currentAccountDto.getBalance(), currentAccountDto.getCustomerDto().getId(), currentAccountDto.getOverDraft()));

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/accounts/ca/"))
                .withRequestBody(WireMock.containing(getJsonAsString(currentAccountDto)))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(currentAccountDtoSaved))));


        HttpPost request = new HttpPost("http://localhost:7070/api/accounts/ca/");
        StringEntity entity = new StringEntity(getJsonAsString(currentAccountDto));
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/api/accounts/ca/")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(currentAccountDtoSaved));
    }

    @Test
    void given_savingAccountDto_not_exists_when_request_saveSavingAccount_then_return_savingAccountDto_with_status_200() throws IOException, CustomerNotFoundException {

        //GIVEN
        SavingAccountDto savingAccountDto = BankAccountPopulator.createSavingAccountDto(UUID.randomUUID().toString(), Math.random() * 900000, new Date(), AccountStatus.CREATED, CustomerMapper.INSTANCE.customerToCustomerDto(persistedCustomer), 9000);
        BankAccountDto savingAccountDtoSaved = SavingAccountMapper.INSTANCE.savingAccountToSavingAccountDto(bankAccountService.saveSavingAccount(savingAccountDto.getBalance(), savingAccountDto.getCustomerDto().getId(), savingAccountDto.getInterestRate()));

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/accounts/sa/"))
                .withRequestBody(WireMock.containing(getJsonAsString(savingAccountDto)))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(savingAccountDtoSaved))));


        HttpPost request = new HttpPost("http://localhost:7070/api/accounts/sa/");
        StringEntity entity = new StringEntity(getJsonAsString(savingAccountDto));
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/api/accounts/sa/")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(savingAccountDtoSaved));
    }

    @Test
    void given_bankAccount_not_exist_when_request_get_accounts_id_executed_then_return_INTERNAL_SERVER_ERROR() throws IOException {

        //GIVEN
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/accounts/100"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(500)));

        HttpUriRequest request = new HttpGet("http://localhost:7070/api/accounts/100");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/accounts/100")));
        assertEquals(500, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());

    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

    private String getJsonAsString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
