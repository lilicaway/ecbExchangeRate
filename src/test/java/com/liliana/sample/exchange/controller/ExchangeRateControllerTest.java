package com.liliana.sample.exchange.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

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

import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDaoMemoryImpl;
import com.liliana.sample.exchange.service.ServiceTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceTestConfiguration.class,
        ControllersTestConfiguration.class })
@WebAppConfiguration
public class ExchangeRateControllerTest {

    private MockMvc mockMvc;

    // We force it to be in memory, so in the future we can use one that uses a
    // real data store and we don't need to change this test
    @Autowired
    private ExchangeRateDaoMemoryImpl exchangeRateDao;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Only one rate on the repository.
        exchangeRateDao.saveExchangeRate(new ExchangeRate(LocalDate.of(2016, 2, 10), "USD",
                new BigDecimal("1.1")));
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
}
