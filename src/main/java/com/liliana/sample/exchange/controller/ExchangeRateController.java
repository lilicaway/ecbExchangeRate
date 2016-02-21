package com.liliana.sample.exchange.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDao;

@Controller
public class ExchangeRateController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExchangeRateDao exchangeRateDao;

    @ResponseBody
    @RequestMapping(value = "/api/exchangeRate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeRate getRateFor(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = true) LocalDate date,
            @RequestParam(required = true) String currencyCode) {

        ExchangeRate exchangeRate = exchangeRateDao
                .retrieveExchangeRate(date, currencyCode);
        if (exchangeRate == null) {
            throw new RateNotFoundException("No rate found for date: '" + date
                    + "' and currencyCode: '" + currencyCode + "'");
        }
        return exchangeRate;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private static class RateNotFoundException extends RuntimeException {

        public RateNotFoundException(String message) {
            super(message);
        }
    }
}
