package com.liliana.sample.exchange.bussiness;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.liliana.sample.exchange.model.CurrencyConversionResult;
import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDao;

@Component
public class CurrencyConverter {
    /** Used for divisions */
    private static final int OPERATIONAL_SCALE = 5;

    /** Used for the results exposed */
    private static final int RESULT_SCALE = 2;

    /**
     * Make sure we use this rounding mode everywhere, since it is the one that
     * statistically minimizes cumulative error when applied repeatedly over a
     * sequence of calculations.
     */
    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

    @Autowired
    @VisibleForTesting
    @Qualifier("exchangeRateDao")
    ExchangeRateDao exchangeRateDao;

    // use system's date as default
    private Clock clock = Clock.systemDefaultZone();

    @VisibleForTesting
    public void setClockForTest(Clock clock) {
        this.clock = clock;
    }

    public CurrencyConversionResult convertFromTo(String fromCurrency, String toCurrency,
            BigDecimal amount, LocalDate date) throws CurrencyNotFoundException {
        date = date == null ? LocalDate.now(clock) : date;
        BigDecimal resultAmount = doConvertFromTo(fromCurrency, toCurrency,
                amount, date);
        return new CurrencyConversionResult(date, amount,
                fromCurrency, resultAmount, toCurrency);
    }

    @VisibleForTesting
    BigDecimal doConvertFromTo(String fromCurrency, String toCurrency,
            BigDecimal amount, LocalDate date) throws CurrencyNotFoundException {
        Objects.requireNonNull(date, "date");

        // First need to go to eur
        BigDecimal amountEuros = convertToEur(fromCurrency, amount, date);
        // Second go with previous call from eur toCurrency
        BigDecimal result = convertFromEur(toCurrency, amountEuros, date);
        // Make sure we round it to the appropriate number of decimals.
        return result.setScale(RESULT_SCALE, ROUNDING);
    }

    /**
     * Returns the amount of money that results from converting an amount from
     * originalCurrency to Euros. The formula used is amount *
     * rate(eur/toCurrency) where the rate comes from the ExchangeRateDao
     * 
     * @param fromCurrency
     *            represents the original currency
     * @param amount
     *            of money represented in original currency
     * @param date
     *            represents the date of the conversion, because rates change
     *            with dates
     * @return amount of money represented in euros
     * @throws CurrencyNotFoundException
     */
    private BigDecimal convertToEur(String fromCurrency, BigDecimal amount, LocalDate date)
            throws CurrencyNotFoundException {
        ExchangeRate rate;
        // amount / rate(fromCurrency/eur) -- rate comes from dao

        rate = exchangeRateDao.retrieveExchangeRate(date, fromCurrency);
        if (rate == null) {
            throw new CurrencyNotFoundException(fromCurrency, date);
        }
        return amount.divide(rate.getRate(), OPERATIONAL_SCALE, ROUNDING);
    }

    /**
     * Returns the amount of money that results from converting an amountEuros
     * from Euro to ToCurrency The formula used is amount * rate(eur/toCurrency)
     * where the rate comes from the ExchangeRateDao
     * 
     * @param toCurrency
     *            represents the destination currency
     * @param amountEuros
     *            amount of money in euros
     * @param date
     *            represents the date of the conversion, because rates change
     *            with dates
     * @return amount of money represented in the Destination currency
     * @throws CurrencyNotFoundException
     */
    private BigDecimal convertFromEur(String toCurrency, BigDecimal amountEuros, LocalDate date)
            throws CurrencyNotFoundException {
        ExchangeRate rate;
        rate = exchangeRateDao.retrieveExchangeRate(date, toCurrency);
        if (rate == null) {
            throw new CurrencyNotFoundException(toCurrency, date);
        }
        return amountEuros.multiply(rate.getRate());
    }

    public static class CurrencyNotFoundException extends IllegalArgumentException {
        private final String currency;
        private final LocalDate date;

        public CurrencyNotFoundException(String currency, LocalDate date) {
            super("Currency '" + currency + "' not found for date: " + date);
            this.currency = currency;
            this.date = date;
        }

        public String getCurrency() {
            return currency;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}
