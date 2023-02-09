package org.sid.ebankingbackend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//@EnableJpaRepositories(basePackages = "org.sid.ebankingbackend.repositories")
@ComponentScan({"org.sid.ebankingbackend.services"})
@Import({JpaConfig.class})
public class InfrastructureTestConfig {
}
