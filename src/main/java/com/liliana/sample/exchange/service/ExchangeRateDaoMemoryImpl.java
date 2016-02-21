package com.liliana.sample.exchange.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.liliana.sample.exchange.model.ExchangeRate;

@Service
public class ExchangeRateDaoMemoryImpl implements ExchangeRateDao {

    private final ConcurrentMap<LocalDate, ConcurrentMap<String, BigDecimal>> data = new ConcurrentHashMap<>();

    @Override
    public void saveExchangeRate(ExchangeRate exchangeRate) {
        ConcurrentMap<String, BigDecimal> dailyData = data.get(exchangeRate.getDate());

        if (dailyData == null) {
            dailyData = new ConcurrentHashMap<>();
            data.put(exchangeRate.getDate(), dailyData);
        }
        dailyData.put(exchangeRate.getCurrencyCode(), exchangeRate.getRate());

    }

    @Override
    public ExchangeRate retrieveExchangeRate(LocalDate date, String currencyCode) {
        ConcurrentMap<String, BigDecimal> map = data.get(date);
        ExchangeRate exchangeRate = null;
        if (map != null) {
            BigDecimal rate = map.get(currencyCode);
            if (rate != null) {
                exchangeRate = new ExchangeRate(date, currencyCode, rate);
            }
        }
        return exchangeRate;
    }

}
