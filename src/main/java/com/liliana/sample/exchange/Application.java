package com.liliana.sample.exchange;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import com.liliana.sample.exchange.service.ExchangeRateDao;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Qualifier("ecbRetryTemplate")
    public RetryTemplate getRetryTemplate() {
        RetryTemplate template = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(Duration.ofSeconds(1).toMillis());
        backOffPolicy.setMaxInterval(Duration.ofHours(5).toMillis());
        template.setBackOffPolicy(backOffPolicy);
        return template;
    }

    @Bean
    @Qualifier("exchangeRateDao")
    public ExchangeRateDao getExchangeRateDao(
            @Qualifier("exchangeRateDaoMySqlImpl") ExchangeRateDao exchangeRateDao) {
        return exchangeRateDao;
    }
}
