package com.liliana.sample.exchange.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.liliana.sample.exchange.bussiness.BussinessTestConfiguration;
import com.liliana.sample.exchange.bussiness.CurrencyConverter;
import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDaoMemoryImpl;
import com.liliana.sample.exchange.service.ServiceTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceTestConfiguration.class,
        ControllersTestConfiguration.class,
        BussinessTestConfiguration.class })
@WebAppConfiguration
public class ExchangeRateControllerTest {

    private MockMvc mockMvc;

    // We force it to be in memory, so in the future we can use one that uses a
    // real data store and we don't need to change this test
    @Autowired
    private ExchangeRateDaoMemoryImpl exchangeRateDao;
    @Autowired
    private CurrencyConverter converter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Clock fixedClock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Only one rate on the repository.
        exchangeRateDao.saveExchangeRate(new ExchangeRate(LocalDate.of(2016, 2, 10), "USD",
                new BigDecimal("1.1")));
        exchangeRateDao.saveExchangeRate(new ExchangeRate(LocalDate.of(2016, 2, 10), "CHF",
                new BigDecimal("1.09")));

        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        exchangeRateDao.saveExchangeRate(new ExchangeRate(LocalDate.now(fixedClock), "GBP",
                new BigDecimal("0.79")));
        exchangeRateDao.saveExchangeRate(new ExchangeRate(LocalDate.now(fixedClock), "CAD",
                new BigDecimal("1.79")));

        converter.setClockForTest(fixedClock);
    }

    @Test
    public void testGetRateForAnExistentValue() throws Exception {
        LocalDate localDate = LocalDate.of(2016, 2, 10);
        String currencyCode = "USD";
        String expectedJson = "{\"date\": \"2016-02-10\",\"currencyCode\": \"USD\",\"rate\": 1.1}";
        mockMvc.perform(
                get("/api/exchangeRate?date={localDate}&currencyCode={currencyCode}", localDate,
                        currencyCode))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetRateForANonExistentValue() throws Exception {
        LocalDate localDate = LocalDate.of(2016, 2, 12);
        String currencyCode = "USD";
        mockMvc.perform(
                get("/api/exchangeRate?date={localDate}&currencyCode={currencyCode}", localDate,
                        currencyCode))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testConvertAmountfromToCurrencies() throws Exception {
        LocalDate localDate = LocalDate.of(2016, 2, 10);
        String fromCurrency = "CHF";
        String toCurrency = "USD";
        String expectedJson = "{\"date\":\"2016-02-10\",\"fromAmount\":30.35,\"fromCurrency\":\"CHF\",\"resultAmount\":30.63,\"toCurrency\":\"USD\"}";

        mockMvc.perform(
                get("/api/convertAmount?amount={amount}&fromCurrency={fromCurrency}&toCurrency={toCurrency}&date={localDate}",
                        30.35d, fromCurrency, toCurrency, localDate
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson.toString()));
    }

    @Test
    public void testConvertAmountfromToCurrenciesDefaultDate() throws Exception {
        String fromCurrency = "CAD";
        String toCurrency = "GBP";
        String expectedJson = "{\"date\":\""
                + LocalDate.now(fixedClock)
                + "\",\"fromAmount\":30.35,\"fromCurrency\":\"CAD\",\"resultAmount\":13.39,\"toCurrency\":\"GBP\"}";

        mockMvc.perform(
                get("/api/convertAmount?amount={amount}&fromCurrency={fromCurrency}&toCurrency={toCurrency}",
                        30.35d, fromCurrency, toCurrency
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson.toString()));
    }

    @Test
    public void testConvertAmountfromToCurrenciesDefaultDateInvalidCurrencies() throws Exception {
        String fromCurrency = "ARS";
        String toCurrency = "GBP";

        mockMvc.perform(
                get("/api/convertAmount?amount={amount}&fromCurrency={fromCurrency}&toCurrency={toCurrency}",
                        30.35d, fromCurrency, toCurrency
                ))
                .andExpect(status().isBadRequest());
    }
}
