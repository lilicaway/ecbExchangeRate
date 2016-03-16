package com.liliana.sample.exchange.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.model.ExchangeRate.ExchangeRateId;
import com.liliana.sample.exchange.service.repository.ExchangeRateRepository;

@Repository
@Qualifier("exchangeRateDaoMySqlImpl")
public class ExchangeRateDaoMySqlImpl implements ExchangeRateDao {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Override
    public void saveExchangeRate(ExchangeRate exchangeRate) {
        exchangeRateRepository.save(exchangeRate);
    }

    @Override
    public ExchangeRate retrieveExchangeRate(LocalDate date, String currencyCode) {
        return exchangeRateRepository.findOne(new ExchangeRateId(date, currencyCode));
    }

    @Override
    public void saveAllExchangeRate(List<ExchangeRate> exchangeRate) {
        exchangeRateRepository.save(exchangeRate);
    }

}
