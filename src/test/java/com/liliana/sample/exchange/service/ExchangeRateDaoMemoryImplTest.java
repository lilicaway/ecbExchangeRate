package com.liliana.sample.exchange.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.liliana.sample.exchange.model.ExchangeRate;

public class ExchangeRateDaoMemoryImplTest {
    ExchangeRateDao exchangeRateDao;

    @Before
    public void setUp() {
        exchangeRateDao = new ExchangeRateDaoMemoryImpl();
    }

    @Test
    public void testSaveAndRetrieveExchangeRate() {
        ExchangeRate expectedExchangeRate = new ExchangeRate(LocalDate.now(),
                Currency.getInstance("CHF").getCurrencyCode(),
                BigDecimal.TEN);
        exchangeRateDao.saveExchangeRate(expectedExchangeRate);

        ExchangeRate actualExchangeRate = exchangeRateDao.retrieveExchangeRate(
                expectedExchangeRate.getDateAsLocalDate(), expectedExchangeRate.getCurrencyCode());

        assertEquals(expectedExchangeRate, actualExchangeRate);
    }
}
