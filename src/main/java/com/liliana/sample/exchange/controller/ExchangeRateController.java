package com.liliana.sample.exchange.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.liliana.sample.exchange.bussiness.CurrencyConverter;
import com.liliana.sample.exchange.bussiness.CurrencyConverter.CurrencyNotFoundException;
import com.liliana.sample.exchange.model.CurrencyConversionResult;
import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.ExchangeRateDao;

@Controller
public class ExchangeRateController {

    @Autowired
    @Qualifier("exchangeRateDao")
    private ExchangeRateDao exchangeRateDao;

    @Autowired
    private CurrencyConverter converter;

    @RequestMapping(value = "/ecbApp")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView ecb(Map<String, Object> model) {
        // model.put("time", new Date());
        // model.put("message", "Hola");
        return new ModelAndView("ecb", model);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/exchangeRate", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/convertAmount", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyConversionResult convertAmountFromToCurrencyForDate(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return converter.convertFromTo(fromCurrency, toCurrency,
                    amount, date);
        } catch (CurrencyNotFoundException e) {
            throw new ConversionException(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private static class RateNotFoundException extends RuntimeException {

        public RateNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private static class ConversionException extends RuntimeException {

        public ConversionException(String message) {
            super(message);
        }
    }
}
