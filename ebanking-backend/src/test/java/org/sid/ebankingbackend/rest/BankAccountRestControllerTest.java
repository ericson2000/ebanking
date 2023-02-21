package org.sid.ebankingbackend.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.sid.ebankingbackend.services.CustomerService;
import org.sid.ebankingbackend.wiremock.WireMockConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

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

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        WireMock.configureFor("localhost", 7070);
    }

    @AfterEach
    void deleteAll() {
        bankAccountRepository.deleteAll();
        wireMockServer.resetAll();
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
}
