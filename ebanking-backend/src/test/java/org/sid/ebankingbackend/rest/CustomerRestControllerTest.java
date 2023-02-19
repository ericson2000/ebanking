package org.sid.ebankingbackend.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sid.ebankingbackend.config.InfrastructureTestConfig;

import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.populator.CustomerPopulator;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.CustomerService;
import org.sid.ebankingbackend.wiremock.WireMockConfig;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = {InfrastructureTestConfig.class, WireMockConfig.class})
@DirtiesContext
public class CustomerRestControllerTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
        WireMock.configureFor("localhost", 7070);
    }

    @AfterEach
    void deleteAll() {
        customerRepository.deleteAll();
        wireMockServer.resetAll();
    }


    @Test
    void given_customer_exist_when_request_get_customers_executed_then_return_list_of_customers() throws Exception {

        //GIVEN
        CustomerDto firstCustomerDto = customerService.saveCustomer(CustomerPopulator.createCustomerDto("eric", "eric@gmail.com"));
        CustomerDto secondCustomerDto = customerService.saveCustomer(CustomerPopulator.createCustomerDto("erica", "eriac@gmail.com"));

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/customers/"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(List.of(firstCustomerDto, secondCustomerDto)))));

        HttpUriRequest request = new HttpGet("http://localhost:7070/api/customers/");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/customers/")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(List.of(firstCustomerDto, secondCustomerDto)));
    }

    @Test
    void given_customer_exist_when_request_get_customer_id_executed_then_return_customer() throws IOException {

        //GIVEN
        CustomerDto customerDto = customerService.saveCustomer(CustomerPopulator.createCustomerDto("eric", "eric@gmail.com"));

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/customers/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(customerDto))));

        HttpUriRequest request = new HttpGet("http://localhost:7070/api/customers/1");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/customers/1")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(customerDto));
    }

    @Test
    void given_customer_not_exist_when_request_post_customer_executed_then_return_customer() throws IOException {

        //GIVEN
        CustomerDto customerDtoToSave = CustomerPopulator.createCustomerDto("eric", "eric@gmail.com");
        CustomerDto customerDtoSaved = customerService.saveCustomer(customerDtoToSave);

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/customers/")).
                withRequestBody(WireMock.containing(getJsonAsString(customerDtoToSave)))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(customerDtoSaved))));

        HttpPost request = new HttpPost("http://localhost:7070/api/customers/");
        StringEntity entity = new StringEntity(getJsonAsString(customerDtoToSave));
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/api/customers/")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(customerDtoSaved));
    }

    @Test
    void given_customer_exist_when_request_put_customer_executed_then_return_customer_updated() throws IOException {

        //GIVEN
        CustomerDto customerDtoSaved = customerService.saveCustomer(CustomerPopulator.createCustomerDto("eric", "eric@gmail.com"));
        customerDtoSaved.setName("ericUpdate");

        WireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/api/customers/1")).
                withRequestBody(WireMock.containing(getJsonAsString(customerDtoSaved)))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getJsonAsString(customerDtoSaved))));

        HttpPut request = new HttpPut("http://localhost:7070/api/customers/1");
        StringEntity entity = new StringEntity(getJsonAsString(customerDtoSaved));
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        String responseString = convertResponseToString(httpResponse);
        WireMock.verify(1, WireMock.putRequestedFor(WireMock.urlEqualTo("/api/customers/1")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals(responseString, getJsonAsString(customerDtoSaved));
    }

    @Test
    void given_customer_exist_when_request_delete_customer_id_executed_then_return_status_200() throws IOException {

        //GIVEN
        CustomerDto customerDto = customerService.saveCustomer(CustomerPopulator.createCustomerDto("eric", "eric@gmail.com"));

        WireMock.stubFor(WireMock.delete(WireMock.urlEqualTo("/api/customers/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        HttpDelete request = new HttpDelete("http://localhost:7070/api/customers/1");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        WireMock.verify(1, WireMock.deleteRequestedFor(WireMock.urlEqualTo("/api/customers/1")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
    }

    @Test
    void given_customer_not_exist_when_request_get_customer_id_executed_then_return_INTERNAL_SERVER_ERROR() throws IOException {

        //GIVEN
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/api/customers/100"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(500)));

        HttpUriRequest request = new HttpGet("http://localhost:7070/api/customers/100");

        //WHEN
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        //THEN
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/customers/100")));
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

    @AfterAll
    void stopServer() {
        wireMockServer.stop();
    }

}
