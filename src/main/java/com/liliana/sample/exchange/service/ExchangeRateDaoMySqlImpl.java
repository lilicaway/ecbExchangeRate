package com.liliana.sample.exchange.service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.stereotype.Repository;

import com.liliana.sample.exchange.model.ExchangeRate;
import com.liliana.sample.exchange.service.repository.ExchangeRateEntity;
import com.liliana.sample.exchange.service.repository.ExchangeRateEntity.ExchangeRateId;
import com.liliana.sample.exchange.service.repository.ExchangeRateRepository;

@Repository
@Qualifier("exchangeRateDaoMySqlImpl")
public class ExchangeRateDaoMySqlImpl implements ExchangeRateDao {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Override
    public void saveExchangeRate(ExchangeRate exchangeRate) {
        ExchangeRateEntity exchangeRateEntity = transformToEntity(exchangeRate);
        exchangeRateRepository.save(exchangeRateEntity);
    }

    private ExchangeRateEntity transformToEntity(ExchangeRate exchangeRate) {
        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();
        exchangeRateEntity.setCurrencyCode(exchangeRate.getCurrencyCode());
        exchangeRateEntity.setRate(exchangeRate.getRate());
        exchangeRateEntity.setDate(Jsr310Converters.LocalDateToDateConverter.INSTANCE
                .convert(exchangeRate.getDate()));
        return exchangeRateEntity;
    }

    @Override
    public ExchangeRate retrieveExchangeRate(LocalDate date, String currencyCode) {
        ExchangeRateEntity entity = exchangeRateRepository.findOne(new ExchangeRateId(
                Jsr310Converters.LocalDateToDateConverter.INSTANCE
                        .convert(date), currencyCode));
        if (entity == null) {
            return null;
        }
        ExchangeRate exchangeRate = new ExchangeRate(
                Jsr310Converters.DateToLocalDateConverter.INSTANCE
                        .convert(entity.getDate()), entity.getCurrencyCode(), entity.getRate());
        return exchangeRate;
    }

    @Override
    public void saveAllExchangeRate(List<ExchangeRate> exchangeRates) {

        Stream<ExchangeRateEntity> map = exchangeRates.stream().map(e -> transformToEntity(e));
        exchangeRateRepository.save(new Iterable<ExchangeRateEntity>() {
            @Override
            public Iterator<ExchangeRateEntity> iterator() {
                return map.iterator();
            }
        });
    }

}
