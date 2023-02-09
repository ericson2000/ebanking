package org.sid.ebankingbackend.rest;

import org.sid.ebankingbackend.wiremock.WireMockConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WireMockConfig.class})
@ComponentScan({"org.sid.ebankingbackend.rest"})
@EnableAutoConfiguration
public class WebClientTestConfigTest {
}
