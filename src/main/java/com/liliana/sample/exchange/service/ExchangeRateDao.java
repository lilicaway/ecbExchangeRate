package com.liliana.sample.exchange.service;

import java.time.LocalDate;

import com.liliana.sample.exchange.model.ExchangeRate;

public interface ExchangeRateDao {

    void saveExchangeRate(ExchangeRate exchangeRate);

    ExchangeRate retrieveExchangeRate(LocalDate date, String currencyCode);
}
