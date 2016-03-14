package com.liliana.sample.exchange.bussiness;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.liliana.sample.exchange.bussiness.CurrencyConverter.CurrencyNotFoundException;
import com.liliana.sample.exchange.model.CurrencyConversionResult;
import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDao;
import com.liliana.sample.exchange.service.ExchangeRateDaoMemoryImpl;

public class CurrencyConverterTest {

    private CurrencyConverter converter;
    private Clock fixedClock;

    @Before
    public void setUp() {
        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        ExchangeRateDao exchangeDao = new ExchangeRateDaoMemoryImpl();
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        exchangeRateList.add(new ExchangeRate(LocalDate.now(fixedClock), "CHF", new BigDecimal(
                "1.10")));
        exchangeRateList.add(new ExchangeRate(LocalDate.now(fixedClock), "GBP", new BigDecimal(
                "0.79")));
        exchangeDao.saveAllExchangeRate(exchangeRateList);
        converter = new CurrencyConverter();
        converter.exchangeRateDao = exchangeDao;
        converter.setClockForTest(fixedClock);
    }

    @Test
    public void testConvertFromCHFToGBPForToday() {
        BigDecimal amountInCHF = new BigDecimal("30");
        BigDecimal expectedResult = new BigDecimal("21.55");

        // Default search is for today
        CurrencyConversionResult result = converter.convertFromTo("CHF", "GBP", amountInCHF, null);

        assertEquals(expectedResult, result.getResultAmount());
    }

    @Test
    public void testConvertFromCHFToGBP() {
        BigDecimal amountInCHF = new BigDecimal("30");
        BigDecimal expectedResult = new BigDecimal("21.55");

        CurrencyConversionResult result = converter.convertFromTo("CHF", "GBP", amountInCHF, null);

        assertEquals(expectedResult, result.getResultAmount());
    }

    @Test
    public void testConvertFromCHFToUSDForYesterday() {
        LocalDate yesterday = LocalDate.now(fixedClock).minus(1, ChronoUnit.DAYS);

        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        BigDecimal usRate = new BigDecimal("0.90");
        exchangeRateList.add(new ExchangeRate(yesterday, "USD", usRate));
        BigDecimal swissRate = new BigDecimal("0.91");
        exchangeRateList.add(new ExchangeRate(yesterday, "CHF", swissRate));
        converter.exchangeRateDao.saveAllExchangeRate(exchangeRateList);

        BigDecimal amountInCHF = new BigDecimal("30");
        BigDecimal appliedFormula = (amountInCHF.divide(swissRate, 5, RoundingMode.HALF_EVEN))
                .multiply(usRate);
        // Get only 2 decimal digits
        BigDecimal expectedResult = appliedFormula.divide(new BigDecimal("1"), 2,
                RoundingMode.HALF_EVEN);

        BigDecimal result = converter.doConvertFromTo("CHF", "USD", amountInCHF, yesterday);

        // Get only 2 decimal digits
        assertEquals(expectedResult, result.divide(new BigDecimal("1"), 2, RoundingMode.HALF_EVEN));
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void testConvertFromNotSupportedCurrencyToUSDForToday() {
        BigDecimal amountInUnsupportedCurrency = new BigDecimal("30");
        converter.convertFromTo("ARS", "USD", amountInUnsupportedCurrency, null);
    }

}
