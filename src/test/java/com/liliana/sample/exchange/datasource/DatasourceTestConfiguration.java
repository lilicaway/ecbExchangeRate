package com.liliana.sample.exchange.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration that takes care of loading the beans from the datasource
 * package
 */
@Configuration
@ComponentScan
public class DatasourceTestConfiguration {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
