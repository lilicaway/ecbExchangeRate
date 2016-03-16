package com.liliana.sample.exchange.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Configuration that takes care of loading the beans from the service package
 */
@Configuration
@ComponentScan(excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { ExchangeRateDaoMySqlImpl.class }))
public class ServiceTestConfiguration {
    @Bean
    @Qualifier("exchangeRateDao")
    public ExchangeRateDao getExchangeRateDao(ExchangeRateDaoMemoryImpl exchangeRateDao) {
        return exchangeRateDao;
    }
}
