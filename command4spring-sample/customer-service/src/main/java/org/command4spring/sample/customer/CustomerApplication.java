package org.command4spring.sample.customer;

import org.command4spring.spring.boot.EnableHttpCommandReceiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
@Import(EnableHttpCommandReceiver.class)
public class CustomerApplication extends SpringBootServletInitializer {


    public static void main(final String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
