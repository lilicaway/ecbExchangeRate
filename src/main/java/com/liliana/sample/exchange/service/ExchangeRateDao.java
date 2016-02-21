package com.liliana.sample.exchange.service;

import java.time.LocalDate;
import java.util.List;

import com.liliana.sample.exchange.model.ExchangeRate;

public interface ExchangeRateDao {

    void saveExchangeRate(ExchangeRate exchangeRate);

    ExchangeRate retrieveExchangeRate(LocalDate date, String currencyCode);

    void saveAllExchangeRate(List<ExchangeRate> exchangeRate);
}
